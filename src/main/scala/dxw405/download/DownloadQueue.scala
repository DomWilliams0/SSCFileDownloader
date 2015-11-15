package dxw405.download

import java.io.File
import java.net.URL
import java.util.concurrent.{Executors, Future}

import dxw405.util.Logging

import scala.collection.mutable

class DownloadQueue(threadCount: Int) {

  private var downloads = mutable.ListBuffer[Future[Boolean]]()
  private lazy val pool = Executors.newFixedThreadPool(threadCount)

  def update(urls: mutable.Seq[String], saveDirectory: File) = {
    // submit to thread pool
    val futures = urls map (s => pool.submit(new DownloadTask(new URL(s), saveDirectory)))

    // add to futures list
    downloads = futures.foldLeft(downloads)(_ += _)
  }

  def close(): Unit = {
    Logging.debug("Shutting down the download thread pool")
    pool.shutdown()
  }


  override def toString = s"DownloadQueue($downloads)"
}
