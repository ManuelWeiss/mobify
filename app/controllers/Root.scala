package controllers

import play.api.mvc._
import play.api.Play.current

// simple controller, answers to GET requests
// returning a literal string as text/plain
object Root extends Controller {

  def index = Action {
    Ok("Domo arigato, Mr Roboto.")
  }

}
