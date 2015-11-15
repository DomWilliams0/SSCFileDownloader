package dxw405.download

import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util.concurrent.Callable

import dxw405.util.Logging
import sys.process._

class DownloadTask(url: URL, saveDirectory: File) extends Callable[Boolean] {

  /**
    * Downloads the given file
    * @return If the download was successful
    */
  override def call(): Boolean = {

    try {
      val file: String = url.getPath.split('/').last
      val targetDir: File = Paths.get(saveDirectory.getAbsolutePath, url.getHost).toFile.getCanonicalFile
      val target: File = Paths.get(targetDir.getAbsolutePath, file).toFile.getCanonicalFile

      targetDir.mkdirs()

      Logging.debug(s"Downloading '$file' to '$target'")

      url #> target !!

    } catch {
      case e: Exception =>
        Logging.error(s"Could not download file from $url", e)
        return false
    }
    true
  }
}
