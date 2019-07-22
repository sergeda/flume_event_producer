package sergey.dashko.services

import java.time.{LocalDate, LocalDateTime}

import sergey.dashko.services.EventProducer.Time

import scala.util.Random

case class Purchase(product: String, category: String, price: Double, at: LocalDateTime, clientIP: String)

class EventProducer(dateRange: Int, productNames: Vector[String], productCategories: Vector[String],
                    productPriceMin: Int, productPriceMax: Int) {

  private val generator = new Random()
  private val FirstHour = 0
  private val LastHour = 23
  private val FirstMinute = 0
  private val LastMinute = 59
  val timesForDay = for(hour <- FirstHour to LastHour; minute <- FirstMinute to LastMinute) yield Time(hour, minute)

  def generatePurchase() = Purchase(
    product = getRandomElement(productNames),
    category = getRandomElement(productCategories),
    price = generateDouble(productPriceMin, productPriceMax, productPriceMax / 2D),
    at = generateDateTime(),
    clientIP = generateIp()
  )

  def generateTime(): Time = timesForDay(Math.round(generateDouble(0, timesForDay.length - 1, timesForDay.length / 2D)).toInt)

  def generateDate(): LocalDate = {
    val now = LocalDate.now()
    now.minusDays(generator.nextInt(dateRange))
  }

  def generateDateTime(): LocalDateTime = {
    val time = generateTime()
    generateDate().atTime(time.hour, time.minute)
  }

  def getIndex(min: Int, max: Int, mean: Int): Int = {
    Math.round(generateDouble(min, max, mean)).toInt
  }

  def generateDouble(min: Int, max: Int, mean: Double): Double = {
    def rec(): Double = {
      val result = generator.nextGaussian() * max + mean
      if(result <= max && result >= min) result else rec()
    }
    rec()
  }

  def getRandomElement[T](list: Seq[T]): T =
    list(generator.nextInt(list.length))

  def generateIp(): String = {for(_ <- 1 to 4) yield generator.nextInt(256)}.mkString(".")
}

object EventProducer {
  case class Time(hour: Int, minute: Int)
}
