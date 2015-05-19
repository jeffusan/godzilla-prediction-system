package core

import java.util.concurrent.TimeUnit
import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import api.{ ErrorResponseException, Marshalling, GodzillaApi }
import spray.http.StatusCodes
import spray.routing._
import spray.util.LoggingContext
import scala.util.control.NonFatal

/**
  * This is the Http Actor that handles URL requests
  */
class ApplicationApiActor(route: Route) extends HttpServiceActor with CustomErrorHandler {

  override def receive: Receive = runRoute(route)(customExceptionHandler, RejectionHandler.Default,
  actorRefFactory, RoutingSettings.default(actorRefFactory), LoggingContext.fromActorContext(actorRefFactory))
}

/**
  * This trait is used to join all APIs.
  * To add a new API, you add it to routes:
  *  val routes = new GodzillaApi().route
  *  ~ new NewAPI().route
  */
trait Api extends RouteConcatenation {

  this: BootSystem =>

  val routes = new GodzillaApi().route

  val routeService = actorSystem.actorOf(Props(new ApplicationApiActor(routes)))
}

/**
  * DefaultTimeout is used to configure the application timeout exception
  */
trait DefaultTimeout {
  implicit val timeout = new Timeout(2, TimeUnit.SECONDS)
}

/**
  * Custom error handler
  * Overriding receive makes this required.
  * also see api.marshalling
  */
trait CustomErrorHandler extends Marshalling {

  implicit def customExceptionHandler(implicit log: LoggingContext): ExceptionHandler = ExceptionHandler.apply {
    case NonFatal(ErrorResponseException(statusCode, entity)) =>
      log.error(s"Application return expected error status code ${statusCode} with entity ${entity} ")
      ctx => ctx.complete((statusCode, entity))
    case NonFatal(e) =>
      log.error(s"Application return unexpected error with exception ${e}")
      ctx => ctx.complete(StatusCodes.InternalServerError)
  }
}
