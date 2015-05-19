package api

import core.DefaultTimeout
import akka.actor.ActorSystem
import spray.routing.Directives
import spray.http.MediaTypes._
import spray.httpx.TwirlSupport
import spray.httpx.encoding.Gzip

/**
  * Sample API for the Godzilla Prediction System
  * Each api path is in a separate spray 'path' directive for easier management
  */
class GodzillaApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout with TwirlSupport {

  // references the assets directory for css, jsx, and application-specific javascript
  val publicAssets = pathPrefix("assets") { fileName =>
    get {
      encodeResponse(Gzip) {
        getFromResource("assets/" + fileName)
      }
    }
  }

  // home page, retrieves compiled twirl template
  val index = path("") {
    get {
      complete(html.index("Hello!"))
    }
  }

  // api - for json
  val api = pathPrefix("api") {
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

  // for webjar javascript dependencies
  val webjars = pathPrefix("webjars") {
    get {
      getFromResourceDirectory("META-INF/resources/webjars")
    }
  }

  val routes = publicAssets ~ index ~ api ~ webjars ~ heat ~ locations ~ getFromResourceDirectory("assets")
}
