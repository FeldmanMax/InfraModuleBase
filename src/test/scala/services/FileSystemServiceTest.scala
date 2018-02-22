package services

import org.scalatest.FunSuite

class FileSystemServiceTest extends FunSuite {
  private val fileService: FileSystemService = new FileSystemService()
  private val file_name: String = "/tmp/some_test.txt"

  test("test flow") {
    fileService.createFile(file_name) match {
      case Left(left) => fail(left)
      case Right(_) => fileService.write(file_name, "here is some data") match {
        case Left(left) => fail(left)
        case Right(_) => fileService.loadFile(file_name) match {
          case Left(left) => fail(left)
          case Right(data) =>
            assert(data.contains("here is some data"))
            fileService.deleteFile(file_name)
        }
      }
    }
  }
}
