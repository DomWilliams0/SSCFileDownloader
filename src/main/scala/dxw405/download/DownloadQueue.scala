package dxw405.download

import java.io.File
import java.net.URL
import java.util.concurrent.Executors

import dxw405.util.Logging

import scala.collection.mutable

class DownloadQueue(threadCount: Int) {

  private val downloads = mutable.ListBuffer[DownloadWrapper]()
  private var saveDirectory: File = null

  private var queue = mutable.ListBuffer[DownloadWrapper]()
  private lazy val pool = Executors.newFixedThreadPool(threadCount)

  def update(urls: mutable.Seq[String], saveDirectory: File) = {
    this.saveDirectory = saveDirectory
    queue = (urls foldLeft queue) ((acc, urlStr) => acc += new DownloadWrapper(new URL(urlStr)))
  }

  def processQueue() = {
    // stop all current downloads
    downloads foreach (_.cancel)
    downloads.clear()

    // submit all to queue
    for (wrapper <- queue) {
      wrapper.future = pool.submit(new DownloadTask(wrapper.fileURL, saveDirectory))
      downloads += wrapper
    }

    // todo poll every 50ms

    downloads
  }

  def close(): Unit = {
    Logging.debug("Shutting down the download thread pool")
    pool.shutdown()
  }


  override def toString = s"DownloadQueue($downloads)"
}
