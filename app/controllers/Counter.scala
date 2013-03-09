package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

// controller answers to GET and POST requests
// GET returns a persistent counter
// POST increments counter
object Counter extends Controller {

  def get = Action {
    Ok(Cache.get("counter") match {
      case Some(c) => c.toString
      case None => "0"
    }).withHeaders("X-HELLO-MOBIFY-ROBOT" -> "hi")
  }

  def increment = Action {
    Cache.get("counter") match {
      case Some(c) => {
        c match {
          case s: Int => {
            Cache.set("counter", s + 1)
            Ok("")
          }
          case _ => Ok("bad value in counter")
        }
      }
      case None => {
        Cache.set("counter", 1)
        Ok("")
      }
    }
  }

}
