package dxw405.download

import java.awt.Component
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util
import java.util.concurrent.{ExecutorCompletionService, Executors}
import javax.swing.{SwingUtilities, SwingWorker}

import dxw405.gui.TaskList
import dxw405.util.Logging

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class DownloadQueue {

	protected final val intermediateResultProperty = "intermediate result"
	private var queue = mutable.ListBuffer[DownloadWrapper]()
	private var threadCount = 1
	private var toggleComponent: Option[Component] = None
	private var site: String = _
	private var saveDirectory: File = _
	private var prepared = false

	/**
	  * Prepares the queue with the given parameters. This must be called before processQueue
	  * @param site The base site to download from
	  * @param urls List of URLs to files to download
	  * @param saveDirectory The directory to save all downloaded files to
	  * @param threadCount The number of threads to use to download
	  * @param toggleComponent An optional component to disable on start, and enable on end
	  */
	def update(site: String, urls: mutable.Seq[String], saveDirectory: File, threadCount: Int, toggleComponent: Option[Component]) = {
		if (threadCount < 1)
			throw new IllegalArgumentException("Invalid number of download threads")

		this.threadCount = threadCount
		this.toggleComponent = toggleComponent
		this.site = site
		this.saveDirectory = saveDirectory

		queue.clear()
		queue = (urls foldLeft queue) ((acc, urlStr) => acc += new DownloadWrapper(new URL(urlStr)))

		prepared = true
	}

	/**
	  * Starts downloading the already prepared queue; update() must be called first
	  * @param taskList The optional GUI task list to update with download progress
	  * @return The list of downloads
	  */
	def processQueue(taskList: Option[TaskList]): ListBuffer[DownloadWrapper] = {
		if (!prepared) {
			Logging.error("Cannot process an unprepared queue!")
			return new ListBuffer[DownloadWrapper]()
		}


		Logging.info(s"Starting ${queue.size} downloads with $threadCount threads")

		// get dir for site
		val url = new URL(site)
		val saveDir = Paths.get(saveDirectory.getAbsolutePath, url.getHost).toFile

		val worker = new DownloadWorker(queue, threadCount, saveDir)
		worker.addPropertyChangeListener(new DownloaderSupervisor(taskList))
		worker.execute()

		// disable component
		toggleComponent.foreach(_.setEnabled(false))

		// if no gui, wait
		if (taskList.isEmpty)
			worker.get()

		prepared = false
		queue
	}

	class DownloaderSupervisor(taskList: Option[TaskList]) extends PropertyChangeListener {
		override def propertyChange(e: PropertyChangeEvent): Unit = {
			val worker = e.getSource.asInstanceOf[DownloadWorker]
			val newValue = e.getNewValue

			// done
			if (newValue == SwingWorker.StateValue.DONE) {
				worker.get()
				toggleComponent.foreach(_.setEnabled(true))
				SwingUtilities.invokeLater(new Runnable {
					override def run(): Unit = taskList.foreach(_.repaint())
				})
			}

			else if (e.getPropertyName == intermediateResultProperty) {
				taskList match {
					case None =>
					case Some(list) => list.setStatus(newValue.asInstanceOf[DownloadWrapper])
				}
			}
		}
	}

	class DownloadWorker(downloads: Seq[DownloadWrapper], threadCount: Int, siteSaveDir: File) extends SwingWorker[Unit, DownloadWrapper] {
		private val pool = Executors.newFixedThreadPool(threadCount)
		private val completion = new ExecutorCompletionService[DownloadWrapper](pool)
		private var current: DownloadWrapper = null

		override def doInBackground(): Unit = {

			// submit tasks
			downloads foreach (dl => completion.submit(new DownloadTask(dl, siteSaveDir)))
			pool.shutdown()

			// publish intermediate results
			for (i <- downloads.indices)
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

	}


}
