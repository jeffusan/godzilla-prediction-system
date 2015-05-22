package service

import akka.actor.{Actor, ActorLogging}
import api.Marshalling
import spray.http.StatusCodes
import spray.json.ProductFormats
import core.SparkConfig._
import scala.util.Try
import scala.concurrent._

case class HeatMapData(samplRate: Double)
case class LocationData(deviation: Int)
case class Heat(latitude: Double, longitude: Double)
case class Location(
    depth: Double, temperature: Double, cast: Long, cruise: String, latitude: Double, longitude: Double)

class GodzillaActor extends Actor with SearchActions with ActorLogging {

  def receive: Receive = {

    case HeatMapData(sampleRate) =>
      log.info("Received request for heat")
      sender ! getHeatMapData(sampleRate)

    case LocationData(deviation) =>
      log.info("Received request for locations")
      sender ! getLocationData(deviation)
  }
}

trait SearchActions {

  val dividend = 1000.toDouble

  def getHeatMapData(sampleRate: Double): Try[List[Heat]] = {

    Try {
      val dataFrame = sqlContext.sql("SELECT * FROM heat")

      dataFrame.sample(false, sampleRate / dividend ).map(row => new Heat(
        row.getDouble(0),
        row.getDouble(1)
      )).collect().toList

    }

  }

  def getLocationData(deviation: Int): Try[List[Location]] = {
    val query = s"""
    SELECT
      T1.depth, temperature, T1.castNumber, T1.cruiseId, T1.latitude, T1.longitude
    FROM (
     SELECT depth, temperature, castNumber, cruiseId,
       latitude, longitude from godzilla) AS T1
     JOIN (
       SELECT avg(temperature) as average, depth from godzilla group by depth
     )  AS T2
     ON T1.depth = T2.depth
     WHERE T1.temperature > T2.average + $deviation
    """
    Try {
      val dataFrame = sqlContext.sql(query)

      dataFrame.map(row => new Location(
        row.getDouble(0),
        row.getDouble(1),
        row.getLong(2),
        row.getString(3),
        row.getDouble(4),
        row.getDouble(5)
      )).collect().toList
    }
  }
}

trait MapFormats extends Marshalling with ProductFormats {

  import spray.json._

  implicit val HeatFormat = jsonFormat2(Heat)
  implicit val LocationFormat = jsonFormat6(Location)
}
