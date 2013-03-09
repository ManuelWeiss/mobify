package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data.Forms._
import play.api.mvc.BodyParsers._
import play.api.libs.json.Json
import play.api.libs.json.JsResultException
import play.api.db.DB
import models._

object Application extends Controller {
  
  def index = Action {
    Ok("Domo arigato, Mr Roboto.")
  }
  
  def get_counter = Action {
    val counter : Option[Any] = Cache.get("counter")
    counter match {
    	case Some(c) => Ok(c.toString).withHeaders("X-HELLO-MOBIFY-ROBOT" -> "hi")
    	case None    => Ok("0").withHeaders("X-HELLO-MOBIFY-ROBOT" -> "hi")
    }
  }
  
  def increment_counter = Action {
    val counter : Option[Any] = Cache.get("counter")
    counter match {
    	case Some(c) => {
    		c match {
    			case s : Int => {
		    		Cache.set("counter", s + 1)
    				Ok("")
    			}
    			case _ => Ok("bad value in counter")
    		}
    	}
    	case None    => {
    		Cache.set("counter", 1)
    		Ok("")
    	}
    }
  }

  def cached_timestamp = Action {
  	val timestamp : Long = Cache.getOrElse[Long]("cached_timestamp", 15) { System.currentTimeMillis / 1000 }
    Ok(timestamp.toString)
  }


	def echo = Action(parse.urlFormEncoded) { request =>
		Ok(Json.toJson(request.body))
	}
	
 
 
 def kvstore_put = Action(parse.json) { request => {
 		// parse data from json
	 	val json = request.body
	 	try {
		 	// insert into db
		 	Item.insert(json.as[Item])
		 	Ok("")
	 	} catch {
	 		case e: JsResultException => BadRequest("invalid JSON")
	 	}
 	}
 }
  
 
 
 def kvstore_post = Action {
 	Item.pop match {
 		case Some(item : Item) => Ok(Json.obj("data" -> item.data))
 		case None => BadRequest("queue is empty")
 	}
 }
 
 
 
 def kvstore_get(id: String) = Action {
 	Item.findById(id) match {
 		case Some(item : Item) => Ok(Json.obj("data" -> item.data))
 		case None => BadRequest("identifier does not exist")
 	}
 }
 
 def list = Action {
 	Ok(Json.toJson(Item.getAll))
 }
 
}