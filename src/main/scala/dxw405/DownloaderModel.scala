package dxw405

import java.awt.Component
import java.io.File
import java.net.URL
import java.util.Observable

import dxw405.download.{DownloadQueue, DownloadWrapper}
import dxw405.gui.TaskList
import dxw405.util.Logging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._
import scala.collection.mutable

class DownloaderModel extends Observable {
	private val fileQueue = new DownloadQueue
	private var _downloads = Seq[DownloadWrapper]()

	def downloads = _downloads

	/**
	  * Gets all file URLs on the given page that match the given file extensions
	  * @param site The URL of the page
	  * @param fileExtensions Non-empty list of file extensions to fetch
	  * @param toggleComponent An optional component to toggle while the operation is taking place
	  * @return Either a buffer of URLs to download, or an exception
	  */
	private def fetchURLs(site: String, fileExtensions: List[String], toggleComponent: Option[Component]): Either[mutable.Buffer[String], Exception] = {
		toggleComponent.foreach(_.setEnabled(false))

		var doc: Document = null
		try {
			doc = Jsoup.connect(site).get()
		} catch {
			case e: Exception => return Right(e)
		}

		val allExtensions = fileExtensions.mkString(".(", "|", ")$]")

		val allLinks = doc.select("a[href~=(?i)" + allExtensions)
		val allImages = doc.select("img[src~=(?i)\\\\?" + allExtensions)

		Logging.debug(s"Scraped ${allLinks.length + allImages.length} files and images")

		Left((allLinks map (_.absUrl("href"))) ++ (allImages map (_.absUrl("src"))))
	}

	/**
	  * Validates the given URL
	  * @param url The URL
	  * @return Some error message, or None if the URL is valid
	  */
	def validateURL(url: String): Option[String] = {
		try {
			if (url.isEmpty)
				return Some("No webpage given")

			val validatedURL = new URL(url)
			if (!validatedURL.getProtocol.startsWith("http"))
				return Some("Protocol must be HTTP or HTTPS")

			None
		} catch {
			case ex: Exception => Some("Invalid URL")
		}
	}

	/**
	  * Attempts to download all files with the given extensions from the given site, and save them to the given directory
	  * @param site The site to download from
	  * @param downloadDirPath The directory to save files to
	  * @param threadCount The number of threads to use
	  * @param fileExtensions A list of file extensions to download
	  * @param taskList Optional GUI list to update
	  * @param toggleComponent Optional component to disable at start, and reenable when done
	  * @return Some error message, or None if the operation succeeded
	  */
	def download(site: String, downloadDirPath: String, threadCount: Int, fileExtensions: List[String],
				 taskList: Option[TaskList], toggleComponent: Option[Component]): Option[String] = {
		// validate save dir
		val saveDir = new File(downloadDirPath)
		if (!saveDir.exists())
			return Some("The supplied save directory doesn't exist")

		// validate URL
		val validationErrorMessage = validateURL(site)
		if (validationErrorMessage.isDefined)
			return validationErrorMessage

		// no file extensions
		if (fileExtensions.isEmpty)
			return Some("You haven't chosen any file extensions to download")

		// fetch urls
		val couldBeURLs = fetchURLs(site, fileExtensions, toggleComponent)
		couldBeURLs match {
			case Right(error) =>
				Logging.error("Could not connect", error)
				toggleComponent.foreach(_.setEnabled(true))
				Some(error.getMessage)
			case Left(urls) =>
				if (urls.isEmpty)
					return Some("There are no files matching your current file extension filter on that site")

				// add to queue
				fileQueue.update(site, urls, saveDir, threadCount, toggleComponent)

				// start downloading
				_downloads = fileQueue.processQueue(taskList) toSeq

				// update observers
				setChanged()
				notifyObservers()

				None
		}
	}


}
