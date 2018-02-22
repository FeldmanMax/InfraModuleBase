package cache

import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits._

class TimedCacheTest extends FunSuite with BeforeAndAfter {
  private var cache: TimedCache[String, String] = _
  before {
    cache = TimedCache.apply[String, String]()
  }

  test("add one member to cache") {
    cache.put("key", "value")
    cache.getWithError("key") match {
      case Left(left) => fail(left)
      case Right(result) => assert(result == "value")
    }
  }

  test("add multiple") {
    cache.put("key_1", "value_1")
    cache.put("key_2", "value_2")
    cache.getAll() match {
      case Left(left) => fail(left)
      case Right(result) => assert(result.contains("value_2"))
    }
  }

  test("update key") {
    cache.put("key_1", "value_1")
    cache.put("key_1", "value_2")
    cache.getWithError("key_1") match {
      case Left(left) => fail(left)
      case Right(result) => assert(result == "value_2")
    }
  }

  test("get or add") {
    cache.getWithError("key") match {
      case Left(_) => cache.getOrAdd("key", "value") match {
        case Left(left) => fail(left)
        case Right(result) => assert(result == "value")
      }
      case Right(_) => fail("existing instance")
    }
  }
}
