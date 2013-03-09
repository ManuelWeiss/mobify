package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
  "Application" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/inexistant_path")) must beNone        
      }
    }
    
    "GET on /" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get
        
        status(home) must beEqualTo(OK)
        contentType(home) must beSome.which(_ == "text/plain")
        contentAsString(home) must contain ("Domo arigato, Mr Roboto.")
      }
    }

    "GET on /get-counter" in {
      running(FakeApplication()) {
        val response = route(FakeRequest(GET, "/get-counter")).get
        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        val value = contentAsString(response).toInt
        header("X-HELLO-MOBIFY-ROBOT", response) must beSome.which(_ == "hi")
      }
    }

    "GET on /cached-timestamp" in {
      running(FakeApplication()) {
        var response = route(FakeRequest(GET, "/cached-timestamp")).get
        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        val value = contentAsString(response).toInt
        value must beGreaterThan(1362795703)
        // get another one
        response = route(FakeRequest(GET, "/cached-timestamp")).get
        var value2 = contentAsString(response).toInt
        value2 must beEqualTo(value)
        Thread.sleep(16)
        response = route(FakeRequest(GET, "/cached-timestamp")).get
        value2 = contentAsString(response).toInt
        value2 must beGreaterThan(value)
      }
    }


  }
}