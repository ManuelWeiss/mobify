package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

// controller answers to GET requests
// returns a cached timestamp
object CachedTimestamp extends Controller {

  val timeout = 15
  def get = Action {
    val timestamp: Long = Cache.getOrElse[Long]("cached_timestamp", timeout) {
      System.currentTimeMillis / 1000
    }
    Ok(timestamp.toString)
  }

}
