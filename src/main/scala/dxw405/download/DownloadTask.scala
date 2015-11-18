package dxw405.download

import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Callable

import dxw405.util.Logging

import scala.sys.process._

class DownloadTask(dl: DownloadWrapper, saveDir: File) extends Callable[DownloadWrapper] {

	override def call(): DownloadWrapper = {

		try {
			val url = dl.fileURL

			val file: String = url.getPath.split('/').last
			val target: File = Paths.get(saveDir.getAbsolutePath, file).toFile.getCanonicalFile

			saveDir.mkdirs()

			// already exists
			if (target.exists()) {
				dl.status = StatusAlreadyExists
				Logging.debug(s"$file already exists, skipping")
				return dl
			}

			dl.status = StatusInProgress
			Logging.debug(s"Downloading '$file' to '$target'")

			url #> target !!

			dl.status = StatusSucceeded

		} catch {
			case e: Exception =>
				Logging.error(s"Could not download file from ${dl.fileURL}", e)
				dl.status = StatusFailed
		}

		dl
	}
}
