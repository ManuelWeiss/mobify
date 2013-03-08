package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current
import play.api.libs.json._

import anorm._
import anorm.SqlParser._

case class Item(id: String, data: String, priority: Long, inserted: Pk[Long] = NotAssigned)

object Item {
  
	// (de-)serialize to/from Json
	implicit object ItemFormat extends Format[Item] {
	  def reads(json: JsValue) = JsSuccess(Item(
	    (json \ "id").as[String],
	    (json \ "data").as[String],
	    (json \ "priority").as[Long]
	  ))
	
	  def writes(item: Item) = JsObject(Seq(
	    "id" -> JsString(item.id),
	    "data" -> JsString(item.data),
	    "priority" -> JsNumber(item.priority)
	  ))
	}
	
  /**
   * Parse an Item from a ResultSet
   */
  val simple = {
    str("items.id") ~
    str("items.data") ~
    get[Long]("items.priority") ~
    get[Pk[Long]]("items.inserted") map {
      case id~data~priority~inserted => Item(id, data, priority, inserted)
    }
  }
  
  /**
   * Get all items.
   */
  def getAll: List[Item] = {
    DB.withConnection { implicit connection =>
      SQL("select * from items").as(Item.simple *)
    }
  }
  
  /**
   * Retrieve an Item by id.
   */
  def findById(id: String): Option[Item] = {
    DB.withConnection { implicit connection =>
      SQL("select * from items where id = {id}").on('id -> id).as(Item.simple.singleOpt)
    }
  }
  
  /**
   * Retrieve and delete Item with highest priority.
   */
  def pop: Option[Item] = {
    DB.withTransaction { implicit connection =>
      val row = SQL(
        """
        	select * from items
        	order by priority desc, inserted asc
        	limit 1
        """
       ).as(Item.simple.singleOpt)
		// now delete the row
       row match {
       		case Some(i: Item) => {
       			SQL("delete from items where id = {id}").on('id -> i.id).executeUpdate()
       			row
       		}
       		case None => None
       }
    }
  }

  /**
   * Insert an item.
   *
   * @param item The Item object.
   */
  def insert(item: Item) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into items values (
            {id}, {data}, {priority}, {inserted}
          )
        """
      ).on(
        'id       -> item.id,
        'data     -> item.data,
        'priority -> item.priority,
        'inserted -> item.inserted
      ).executeUpdate()
    }
  }

}

