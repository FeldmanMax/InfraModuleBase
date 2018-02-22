package logger

import org.scalatest.FunSuite
import org.slf4j.event.Level
import services.FileSystemService

class ApplicationLoggerTest extends FunSuite {
  test("read/write into info/error/debug files") {
    val fileService: FileSystemService = new FileSystemService()
    val fileLogger: FileLogger = new FileLogger
    val data: String = "here is my info"
    fileService.createFile("/tmp/log_info_test.log")
    fileService.createFile("/tmp/log_error_test.log")
    fileService.createFile("/tmp/log_debug_test.log")
    fileLogger.log(Level.INFO, data)
    fileLogger.log(Level.DEBUG, data)
    fileLogger.log(Level.ERROR, data)
    checkFile(fileService, data, "info")
    checkFile(fileService, data, "debug")
    checkFile(fileService, data, "error")
    fileService.deleteFiles("/tmp", "log_.*".r)
  }

  private def checkFile(fileService: FileSystemService, data: String, level: String) = {
    val fileToRead: String = s"/tmp/log_${level}_test.log"
    fileService.loadFile(fileToRead) match {
      case Left(left) => fail(left)
      case Right(response) => assert(response.contains(data), s" ---> $level failed")
    }
  }
}
