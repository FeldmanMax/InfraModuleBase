package http

trait ResultType

abstract class HttpErrorType extends ResultType
object Success              extends HttpErrorType // 2xx
object Redirection          extends HttpErrorType // 3xx
object ClientErrors         extends HttpErrorType // 4xx
object ServerErrors         extends HttpErrorType // 5xx