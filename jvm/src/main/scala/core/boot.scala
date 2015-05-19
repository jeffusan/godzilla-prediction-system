package core

import java.util.concurrent.TimeUnit
import akka.actor.{ Props, ActorSystem }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{ ConfigFactory, Config }
import spray.can.Http
import scala.concurrent.Await

/**
  * This is the main application launcher.
  * It defines the actor system, creates a server instance, and adds a shutdown hook.
  */
object Boot {

  implicit val system = ActorSystem("godzilla-prediction-system")

  def main(args: Array[String]) {
    class ApplicationServer(val actorSystem: ActorSystem) extends BootSystem with Api with ServerIO
    new ApplicationServer(system)

    sys.addShutdownHook(system.shutdown())
  }
}

trait ServerIO {

  this: Api with BootSystem =>

  val config = ConfigFactory.load()

  IO(Http) ! Http.Bind(routeService, config.getString("application.server.host"), config.getInt("application.server.port"))
}

trait BootSystem {

  final val startupTimeout = 15

  implicit def actorSystem: ActorSystem
  implicit val timeout: Timeout = Timeout(startupTimeout, TimeUnit.SECONDS)

  val application = actorSystem.actorOf(Props[ApplicationActor], "gds")
  Await.ready(application ? Startup(), timeout.duration)

  actorSystem.registerOnTermination {
    application ! Shutdown()
  }
}
