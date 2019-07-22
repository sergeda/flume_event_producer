package sergey.dashko

import java.net.InetAddress
import java.time.ZoneOffset

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.{Config, ConfigFactory}
import sergey.dashko.services.{EventProducer, EventSender}

import scala.collection.JavaConverters._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val config: Config = ConfigFactory.load()
    val products = config.getStringList("product-names").asScala.toVector
    val categories = config.getStringList("product-categories").asScala.toVector
    val productPriceMin = config.getInt("product-price-min")
    val productPriceMax = config.getInt("product-price-max")
    val flumePort = config.getInt("flume-port")
    val flumeIp = InetAddress.getByName(config.getString("flume-ip"))
    val dateRange = config.getInt("date-range")
    val numberOfEventsToGenerate = config.getInt("total-events-generate")
    val eventProducer = new EventProducer(dateRange, products, categories, productPriceMin, productPriceMax)
    val eventSender = new EventSender(flumeIp, flumePort)

    val events = for(_ <- 1 to numberOfEventsToGenerate) yield {
      val event = eventProducer.generatePurchase()
      List(event.product, event.price, event.at.toEpochSecond(ZoneOffset.UTC) * 1000, event.category, event.clientIP).mkString(",")
    }

    eventSender.sendEvents(events).map(_ => ExitCode.Success)
  }

}
