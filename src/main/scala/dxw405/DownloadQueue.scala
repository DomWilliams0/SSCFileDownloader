package dxw405

import java.util.concurrent.Executors

import dxw405.util.Logging

import scala.collection.mutable

class DownloadQueue(threadCount: Int) {

  private var queue = mutable.Queue[String]()
  private lazy val pool = Executors.newFixedThreadPool(threadCount)

  def update(urls: mutable.Seq[String]) = {
    queue.clear()
    queue = urls.foldLeft(queue)(_ += _)
  }

  def close(): Unit = {
    Logging.debug("Shutting down the download thread pool")
    pool.shutdown()
  }

}
