package dxw405.gui

import java.awt.{BorderLayout, Component}
import java.util.{Observable, Observer}
import javax.swing._

import dxw405.DownloaderModel
import dxw405.download.DownloadWrapper

import scala.collection.mutable.ListBuffer

class TaskList(downloaderModel: DownloaderModel) extends JPanel with Observer {

  private val model = downloaderModel

  private val taskList = new JList[DownloadWrapper]()
  taskList.setModel(new TaskListModel)
  taskList.setCellRenderer(new TaskListRenderer)
  taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

  {
    val scrollPane = new JScrollPane(taskList)

    setLayout(new BorderLayout)
    add(scrollPane, BorderLayout.CENTER)
  }


  class TaskListModel extends DefaultListModel[DownloadWrapper] {

    private val downloads = new ListBuffer[DownloadWrapper]()

    def update(downloads: DownloadWrapper) = ???

    /**
      * Fills the list with the given downloads
      * @param newDownloads The new downloads
      */
    def update(newDownloads: Seq[DownloadWrapper]) = {
      newDownloads.copyToBuffer(downloads)
      fireContentsChanged(this, 0, newDownloads.size)
    }

    override def getSize: Int = downloads.size

    override def getElementAt(index: Int): DownloadWrapper = downloads(index)
  }

  class TaskListRenderer extends JPanel with ListCellRenderer[DownloadWrapper] {
    private val label = new JLabel

    add(label)

    override def getListCellRendererComponent(list: JList[_ <: DownloadWrapper], value: DownloadWrapper,
                                              index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {

      setBackground(value.status.colour)

      label.setText(f"${value.fileURL.getPath}: ${value.status.pretty}")
      this
    }
  }

  override def update(o: Observable, arg: scala.Any): Unit = {

    arg match {
      // reset all
      case newDownloads: Seq[DownloadWrapper] => taskList.getModel.asInstanceOf[TaskListModel].update(newDownloads)

      case _ =>
    }

  }
}
