package dxw405.download

import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Callable

import dxw405.util.{Config, Logging}

import scala.sys.process._
import scala.util.Random

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

			// work simulation, for testing
			if (Config.getBoolean("debug.work-simulation.enabled"))
				Thread.sleep(Random.nextInt(Config.getInt("debug.work-simulation.max-duration")))

			dl.status = StatusSucceeded

		} catch {
			case e: Exception =>
				Logging.error(s"Could not download file from ${dl.fileURL}", e)
				dl.status = StatusFailed
		}

		dl
	}
}
