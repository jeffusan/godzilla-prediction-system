package core

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import com.typesafe.config.{ ConfigFactory, Config }


object SparkConfig {

  val config = ConfigFactory.load()

  @transient val sc = new SparkConf()
    .setMaster(config.getString("application.spark.master-uri"))
    .setAppName(config.getString("application.spark.app-name"))

  val sqlContext = new SQLContext(new SparkContext(sc))

  def init() = {
    sqlContext.jsonFile("src/main/resources/data.json").registerTempTable("godzilla")
    val heatFrame = sqlContext.sql("SELECT latitude, longitude FROM godzilla")
    heatFrame.registerTempTable("heat")
  }

}
