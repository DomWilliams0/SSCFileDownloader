package dxw405

import org.jsoup.Jsoup

import scala.collection.JavaConversions._
import scala.collection.mutable

class DownloaderModel
{
	/**
	  * Gets all image URLs on the given page
	  * @param site The URL of the page
	  * @return A buffer of all image URLs
	  */
	def getImages(site: String): mutable.Buffer[String] =
	{
		val doc = Jsoup.connect(site).get()
		val images = doc.select("img[src~=(?i)\\\\?.(png|jpe?g|gif)]")

		images map (im => im.baseUri() + im.attr("src"))
	}

}
