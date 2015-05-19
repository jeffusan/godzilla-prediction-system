package api

import core.DefaultTimeout

/**
  * Sample API for the Godzilla Prediction System
  * Each api path is in a separate spray 'path' directive
  */
class GodzillaApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout {

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
    }
}
