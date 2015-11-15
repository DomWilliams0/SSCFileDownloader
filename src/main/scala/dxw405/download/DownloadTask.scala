package dxw405.download

import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util.concurrent.Callable

import dxw405.util.Logging

import scala.sys.process._

class DownloadTask(url: URL, saveDirectory: File) extends Callable[DownloadWrapper] {

  override def call(): DownloadWrapper = {

    val dl = new DownloadWrapper(url)

    try {
      val file: String = url.getPath.split('/').last
      val targetDir: File = Paths.get(saveDirectory.getAbsolutePath, url.getHost).toFile.getCanonicalFile
      val target: File = Paths.get(targetDir.getAbsolutePath, file).toFile.getCanonicalFile

      targetDir.mkdirs()

      dl.status = StatusInProgress
      Logging.debug(s"Downloading '$file' to '$target'")

      url #> target !!

      dl.status = StatusSucceeded

    } catch {
      case e: Exception =>
        Logging.error(s"Could not download file from $url", e)
        dl.status = StatusFailed
    }

    dl
  }
}
