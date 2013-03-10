package controllers

import play.api.mvc._
import models._
import play.api.Play.current

// controller answers to GET and POST requests
// GET returns a persistent counter
// POST increments counter
// (see Models.scala for PersistentCounter class)
object Counter extends Controller {

  def get = Action {
    Ok(PersistentCounter.get.toString).withHeaders("X-HELLO-MOBIFY-ROBOT" -> "hi")
  }

  def increment = Action {
    PersistentCounter.increment
    Ok("")
  }

}
