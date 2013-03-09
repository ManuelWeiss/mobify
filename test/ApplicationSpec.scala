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
        
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/plain")
        contentAsString(home) must contain ("Domo arigato, Mr Roboto.")
      }
    }

    "GET on /get-counter" in {
      running(FakeApplication()) {
        val counter = route(FakeRequest(GET, "/get-counter")).get
        
        status(counter) must equalTo(OK)
        contentType(counter) must beSome.which(_ == "text/plain")
        val value = contentAsString(counter)
        header("X-HELLO-MOBIFY-ROBOT", counter) must beSome.which(_ == "hi")
      }
    }




  }
}