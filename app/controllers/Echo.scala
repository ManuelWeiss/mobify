package controllers

import play.api.mvc._
import play.api.libs.json.Json
import play.api.Play.current

// controller answers to POST requests returning
// the posted form fields in Json format
object Echo extends Controller {

  def call = Action(parse.urlFormEncoded) {
    request =>
      Ok(Json.toJson(request.body))
  }

}
