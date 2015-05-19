package api

import core.DefaultTimeout
import akka.actor.ActorSystem
import spray.routing.Directives
import spray.http.MediaTypes._
import spray.httpx.TwirlSupport
import spray.httpx.encoding.Gzip
import service.{MapFormats, HeatMapData, LocationData, Heat, Location}
import akka.pattern.ask

/**
  * Sample API for the Godzilla Prediction System
  * Each api path is in a separate spray 'path' directive for easier management
  */
class GodzillaApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout with TwirlSupport with MapFormats {

  import scala.concurrent.ExecutionContext.Implicits.global
  val godzillaActor = actorSystem.actorSelection("/user/gds/godzilla")

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

  val heat = path("heat") {
    get {
      complete {
        (godzillaActor ? HeatMapData()).mapTo[List[Heat]]
      }
    }
  }

  val locations = path("locations") {
    get {
      complete {
        (godzillaActor ? LocationData()).mapTo[List[Location]]
      }
    }
  }

  // for webjar javascript dependencies
  val webjars = pathPrefix("webjars") {
    get {
      getFromResourceDirectory("META-INF/resources/webjars")
    }
  }

  val routes = publicAssets ~ index ~ heat ~ locations ~ webjars ~ getFromResourceDirectory("assets")
}
