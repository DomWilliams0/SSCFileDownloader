package dxw405.gui

import java.awt._
import java.util.{Observable, Observer}
import javax.swing._
import javax.swing.border.EmptyBorder

import dxw405.DownloaderModel
import dxw405.download.DownloadWrapper
import dxw405.util.Config

import scala.collection.mutable.ListBuffer

class TaskList(model: DownloaderModel) extends JPanel with Observer {

	private val taskList = new JList[DownloadWrapper]()
	private val scrollPane = new JScrollPane(taskList)
	private val taskListModel = new TaskListModel

	taskList.setModel(taskListModel)
	taskList.setCellRenderer(new TaskListRenderer)
	taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

	setLayout(new BorderLayout)
	add(scrollPane, BorderLayout.CENTER)

	override def update(o: Observable, arg: scala.Any): Unit = {
		taskListModel.update(model.downloads)
	}

	def setStatus(newValue: DownloadWrapper) = {
		taskListModel.update(newValue)

		val scrollIndex = Math.min(taskListModel.getIndex(newValue) + 1, taskListModel.getSize - 1)
		taskList.ensureIndexIsVisible(scrollIndex)
	}


	class TaskListModel extends DefaultListModel[DownloadWrapper] {

		private val downloads = new ListBuffer[DownloadWrapper]()

		/**
		  * Fills the list with the given downloads
		  * @param newDownloads The new downloads
		  */
		def update(newDownloads: Seq[DownloadWrapper]) = {
			downloads.clear()
			newDownloads.copyToBuffer(downloads)
			fireContentsChanged(this, 0, newDownloads.size)
		}

		override def getSize: Int = downloads.size

		override def getElementAt(index: Int): DownloadWrapper = downloads(index)

		def update(dl: DownloadWrapper): Unit = {
			val index = downloads.indexOf(dl)
			if (index > 0)
				fireContentsChanged(this, index, index)
		}

		def getIndex(dl: DownloadWrapper): Int = downloads.indexOf(dl)

	}

	class TaskListRenderer extends JPanel with ListCellRenderer[DownloadWrapper] {
		private val selected = new Color(154, 198, 255)

		private val fileName = new JLabel("", SwingConstants.LEFT)
		private val status = new JLabel("", SwingConstants.CENTER)

		private val maxLength = Config.getInt("gui.max-url-length")

		setLayout(new BorderLayout)
		setBorder(new EmptyBorder(0, 10, 0, 10))
		add(fileName, BorderLayout.CENTER)
		add(status, BorderLayout.EAST)

		override def getListCellRendererComponent(list: JList[_ <: DownloadWrapper], value: DownloadWrapper,
												  index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {

			def limit(s: String, length: Int): String = {
				if (s.length <= length)
					return s

				s.substring(0, if (length < 3) length else length - 3) + "..."
			}

			setBackground(if (isSelected) selected else value.status.colour)

			fileName.setText(limit(value.fileURL.getFile, maxLength))
			fileName.setToolTipText(s"URL: ${value.fileURL.toExternalForm}")

			status.setText(s"<html><b>${value.status.pretty}</b></html>")

			this
		}
	}


}
