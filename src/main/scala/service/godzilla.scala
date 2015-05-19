package service

import akka.actor.{Actor, ActorLogging}
import api.Marshalling
import spray.http.StatusCodes
import spray.json.ProductFormats
import core.SparkConfig._

case class HeatMapData()
case class LocationData()
case class Heat(latitude: Double, longitude: Double)
case class Location(
    depth: Double, temperature: Double, cast: Long, cruise: String, latitude: Double, longitude: Double)

class GodzillaActor extends Actor with SearchActions with ActorLogging {

  def receive: Receive = {

    case HeatMapData() =>
      log.info("Received request for heat")
      sender ! getHeatMapData()

    case LocationData() =>
      sender ! getLocationData()
  }
}

trait SearchActions {

  def getHeatMapData(): List[Heat] = {

    val query = """
    SELECT
    latitude, longitude from godzilla
    """
    val dataFrame = sqlContext.sql(query)

    dataFrame.map(row => new Heat(
      row.getDouble(0),
      row.getDouble(1)
    )).collect().toList

  }

  def getLocationData(): List[Location] = {
    val query = """
    SELECT
      T1.depth, T1.temperature, T1.castNumber, T1.cruiseId, T1.latitude, T1.longitude
    FROM (
     SELECT depth, temperature, castNumber, cruiseId,
       latitude, longitude from godzilla) AS T1
     JOIN (
       SELECT avg(temperature) as average, depth from godzilla group by depth
     )  AS T2
     ON T1.depth = T2.depth
     WHERE T1.temperature > T2.average + 12
    """
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

trait MapFormats extends Marshalling with ProductFormats {

  import spray.json._

  implicit val HeatFormat = jsonFormat2(Heat)
  implicit val LocationFormat = jsonFormat6(Location)
}
