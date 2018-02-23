package http

import dwrapper.image.traits.DockerExecutor
import exceptions.HttpGeneralException
import models.KeyValue
import org.scalatest.{BeforeAndAfter, FunSuite}

class HttpCallTest extends FunSuite with DockerExecutor with HttpBase with BeforeAndAfter {
  private var timeouts: Int = _
  private var uri: String = _
  private var port: Int = _

  before {
    timeouts = 500
    uri = "http://localhost"
    port = 80
  }

  test("all http requests with docker") {
    val dockerFilePath: String = "/Users/maksik1/IdeaProjects/ApiResponse"
    dockerExecute("http-calls-2.0.0", "image/web-api", "2.0.0", dockerFilePath, Some(Map("80" -> "9000"))) ({ () =>
      val results: List[Either[String, Unit]] = runTestList()
      if(results.exists(_.isLeft))  Option(results.filter(_.isLeft).map(_.left.get) mkString "\n")
      else                          None
    }, webServerWarmUp(uri, Option(port))) match {
      case None => assert(1 == 1)
      case Some(error) => fail(error)
    }
  }

  private def runTestList(): List[Either[String, Unit]] = {
    List(
      test200(),
      test500(),
      testTimeout()
    )
  }

  private def test200(): Either[String, Unit] = {
    val code: Int = 200
    val httpCall = new HttpCall
    val metadata: HttpRequestMetadata = HttpRequestMetadata(s"$uri:$port/http/get/200", timeouts, timeouts, Map.empty)
    val response = httpCall.get(metadata)
    response match {
      case Left(left) => Left(left.getMessage)
      case Right(httpResponse) => if(httpResponse.code == code)   Right()
                                  else                            Left(s"Expected: $code, Actual: ${httpResponse.code}")
    }
  }

  private def test500(): Either[String, Unit] = {
    val code: Int = 500
    val httpCall = new HttpCall
    val metadata: HttpRequestMetadata = HttpRequestMetadata("http://localhost:80/http/get/500", timeouts, timeouts, Map.empty)
    val response = httpCall.get(metadata)
    response match {
      case Left(left) => Left(left.getMessage)
      case Right(httpResponse) => if(httpResponse.code == code)   Right()
                                  else                            Left(s"Expected: $code\nActual: ${httpResponse.code}\n")

    }
  }

  private def testTimeout(): Either[String, Unit] = {
    var errors_collector: List[String] = List.empty
    val httpCall = new HttpCall
    val metadata: HttpRequestMetadata = HttpRequestMetadata("http://localhost:80/http/get/delay/1000", timeouts, timeouts, Map.empty)
    val response = httpCall.get(metadata)
    response match {
      case Right(_) => errors_collector = errors_collector ::: List[String]("Should have failed")
      case Left(exception) => exception match {
        case ex: HttpGeneralException[String] =>
          val request: KeyValue[String] = ex.key_value_list.find(x=>x.key == "request").getOrElse(KeyValue[String]("", ""))
          val error: KeyValue[String] = ex.key_value_list.find(x=>x.key == "error").getOrElse(KeyValue[String]("", ""))
          if(request.value != metadata.toString)  errors_collector = errors_collector ::: List(s"Expected: ${metadata.toString}\nActual: ${request.value}\n")
          if(error.value != "Read timed out")     errors_collector = errors_collector ::: List(s"Expected: Read timed out\nActual: ${error.value}\n")
        case ex: Exception => Left(ex.toString)
      }
    }
    if(errors_collector.isEmpty)  Right()
    else                          Left(errors_collector mkString "\n")
  }
}