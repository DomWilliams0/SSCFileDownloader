package dxw405.gui

import java.awt.{BorderLayout, Component}
import javax.swing._

import dxw405.DownloaderModel
import dxw405.download.DownloadWrapper

import scala.collection.mutable.ListBuffer

class TaskList(downloaderModel: DownloaderModel) extends JPanel {

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

}
