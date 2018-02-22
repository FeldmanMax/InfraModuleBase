package models

case class KeyValue[T](key: String, value: T) {
  override def toString: String = s"key: $key, value: ${value.toString}"
}
