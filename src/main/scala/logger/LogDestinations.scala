package logger

trait LogDestination

object DoNotLog extends LogDestination
object FileDestination extends LogDestination
