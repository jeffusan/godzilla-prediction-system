package api

import core.DefaultTimeout
import akka.actor.ActorSystem
import spray.routing.Directives
import spray.http.MediaTypes._
import spray.httpx.TwirlSupport

/**
  * Sample API for the Godzilla Prediction System
  * Each api path is in a separate spray 'path' directive
  */
class GodzillaApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout with TwirlSupport {

  val route =
    path("") {
      get {
        respondWithMediaType(`application/json`) {
          _.complete {
            """
            [
             {"name": "kitano", "id": 1},
             {"name": "nguyen", "id": 2 }
            ]
            """
          }
        }
      }
    } ~
  path("godzilla") {
    get {
      complete(html.index())
    }
  }
}
