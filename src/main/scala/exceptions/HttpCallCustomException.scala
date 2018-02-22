package exceptions

import models.KeyValue

case class HttpInvalidRequest[T](keyValue: List[KeyValue[T]]) extends Exception
case class HttpGeneralException[T](keyValue: List[KeyValue[T]]) extends Exception