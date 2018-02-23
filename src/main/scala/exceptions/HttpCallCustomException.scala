package exceptions

import models.KeyValue

case class HttpInvalidRequest[T](key_value_list: List[KeyValue[T]]) extends Exception
case class HttpGeneralException[T](key_value_list: List[KeyValue[T]]) extends Exception