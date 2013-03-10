package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.test.FakeApplication

class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/inexistant_path")) must beNone
      }
    }

    "Domo arigato" in {
      running(FakeApplication()) {
        val response = route(FakeRequest(GET, controllers.routes.Root.index().url)).get

        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        contentAsString(response) must be_==("Domo arigato, Mr Roboto.")
      }
    }

    "counter" in {
      running(FakeApplication()) {
        var response = route(FakeRequest(GET, controllers.routes.Counter.get().url)).get

        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        contentAsString(response).toInt mustEqual 0
        header("X-HELLO-MOBIFY-ROBOT", response) must beSome.which(_ == "hi")

        // now increment via POST
        response = route(FakeRequest(POST, controllers.routes.Counter.increment().url)).get

        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        contentAsString(response) mustEqual ""

        // check new value
        response = route(FakeRequest(GET, controllers.routes.Counter.get().url)).get

        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        contentAsString(response).toInt mustEqual 1
        header("X-HELLO-MOBIFY-ROBOT", response) must beSome.which(_ == "hi")
      }
    }

    "cached-timestamp" in {
      running(FakeApplication()) {
        var response = route(FakeRequest(GET, controllers.routes.CachedTimestamp.get().url)).get

        status(response) must beEqualTo(OK)
        contentType(response) must beSome.which(_ == "text/plain")
        val value = contentAsString(response).toInt
        value must beGreaterThan(1362916989)  // 2013-03-10

        // get another one
        response = route(FakeRequest(GET, controllers.routes.CachedTimestamp.get().url)).get

        var value2 = contentAsString(response).toInt
        value2 mustEqual value

        // now wait for a while to get a new one
        Thread.sleep(16 * 1000)
        response = route(FakeRequest(GET, controllers.routes.CachedTimestamp.get().url)).get

        value2 = contentAsString(response).toInt
        value2 must beGreaterThan(value)
      }
    }
  }

  "echo" in {
    running(FakeApplication()) {
      val response = route(FakeRequest(POST, controllers.routes.Echo.call().url).withFormUrlEncodedBody("foo" -> "bar")).get
      status(response) must beEqualTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
      contentAsString(response) mustEqual "{\"foo\":[\"bar\"]}"
    }
  }
}