package dxw405.download

import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.io.File
import java.net.URL
import java.util
import java.util.concurrent.{ExecutorCompletionService, Executors}
import javax.swing.SwingWorker

import dxw405.gui.TaskList
import dxw405.util.Logging

import scala.collection.JavaConversions._
import scala.collection.mutable

class DownloadQueue {

	protected final val intermediateResultProperty = "intermediate result"
	private var queue = mutable.ListBuffer[DownloadWrapper]()
	private var threadCount = 1

	def update(urls: mutable.Seq[String], saveDirectory: File, threadCount: Int) = {
		this.threadCount = threadCount
		queue.clear()
		queue = (urls foldLeft queue) ((acc, urlStr) => acc += new DownloadWrapper(new URL(urlStr), saveDirectory))
	}

	def processQueue(taskList: Option[TaskList]) = {
		Logging.info(s"Starting ${queue.size} downloads with $threadCount threads")

		val worker = new DownloadWorker(queue, threadCount)
		worker.addPropertyChangeListener(new DownloaderSupervisor(taskList))
		worker.execute()
		queue
	}

	class DownloaderSupervisor(taskList: Option[TaskList]) extends PropertyChangeListener {
		override def propertyChange(e: PropertyChangeEvent): Unit = {
			val worker = e.getSource.asInstanceOf[DownloadWorker]
			val newValue = e.getNewValue

			// done
			if (newValue == SwingWorker.StateValue.DONE) {

				// todo reenable download button
				worker.get()
			}

			else if (e.getPropertyName == intermediateResultProperty) {
				taskList match {
					case None =>
					case Some(list) => list.setStatus(newValue.asInstanceOf[DownloadWrapper])
				}
			}
		}
	}

	class DownloadWorker(downloads: Seq[DownloadWrapper], threadCount: Int) extends SwingWorker[Unit, DownloadWrapper] {
		private val pool = Executors.newFixedThreadPool(threadCount)
		private val completion = new ExecutorCompletionService[DownloadWrapper](pool)
		private var current: DownloadWrapper = null

		override def doInBackground(): Unit = {

			// submit tasks
			downloads foreach (dl => completion.submit(new DownloadTask(dl)))

			// publish intermediate results
			for (i <- 0 to downloads.size)
				publish(completion.take().get())

			// todo progress bar and timeout
		}

		override def process(chunks: util.List[DownloadWrapper]): Unit =
			(chunks toList) foreach updateIntermediateResult

		private def updateIntermediateResult(result: DownloadWrapper): Unit = {
			val oldV = current
			val newV = result
			current = newV
			firePropertyChange(DownloadQueue.this.intermediateResultProperty, oldV, newV)
		}

		override def done(): Unit = {
			pool.shutdown()
		}
	}


}
