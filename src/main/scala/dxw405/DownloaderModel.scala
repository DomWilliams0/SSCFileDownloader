package dxw405

import java.io.File
import java.net.URL
import java.util.Observable

import dxw405.download.DownloadQueue
import dxw405.util.Logging
import org.jsoup.Jsoup

import scala.collection.JavaConversions._
import scala.collection.mutable

class DownloaderModel extends Observable {
  private val fileQueue = new DownloadQueue(5)

  /**
    * Gets all image URLs on the given page
    * @param site The URL of the page
    * @return A buffer of all image URLs
    */
  private def getImages(site: String): mutable.Buffer[String] = {
    val doc = Jsoup.connect(site).get()
    val images = doc.select("img[src~=(?i)\\\\?.(png|jpe?g|gif)]")

    Logging.debug(f"Scraped ${images.length} images")
    images map (_.absUrl("src"))
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
    * Attempts to download all images from the given site, and save them to the given directory
    * @param site The site to download from
    * @param downloadDirPath The directory to save files to
    * @return Some error message, or None if the operation succeeded
    */
  def download(site: String, downloadDirPath: String): Option[String] = {
    // validate save dir
    val saveDir = new File(downloadDirPath)
    if (!saveDir.exists())
      return Some("The supplied save directory doesn't exist")

    // validate URL
    val validationErrorMessage = validateURL(site)
    if (validationErrorMessage.isDefined)
      return validationErrorMessage

    // fetch urls
    val urls = getImages(site)

    // add to queue
    fileQueue.update(urls, saveDir)


    // todo start downloading

    None
  }

  def close(): Unit = fileQueue.close()

}
