package logger

import com.typesafe.config.{Config, ConfigFactory}

object LoggerConfiguration {
  private lazy val config: Config = ConfigFactory.load()

  def loggingDestinations: List[LogDestination] = {
    try {
      config.getString("loggingDestinations").split(",").toList.map { single =>
        single.toLowerCase match {
          case "file"     => FileDestination
          case "donotlog" => DoNotLog
          case _          => DoNotLog
        }
      }
    }
    catch {
      case _: Exception => List(DoNotLog)
    }
  }
}