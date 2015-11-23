package dxw405.gui

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, Component}
import javax.swing._

import dxw405.gui.util.TextFieldPlaceholder
import dxw405.util.Config

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class FileExtensionList extends JPanel {
	private val listModel = new FileExtensionListModel
	private val extList = new JList[String](listModel)

	private val extField = new TextFieldPlaceholder("Enter extra file extensions here")
	private val extRegex = "^[a-zA-Z]+$".r

	extList.setCellRenderer(new FileExtensionListRenderer)
	extList.setSelectionInterval(0, listModel.getSize - 1)


	extField.addActionListener(new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = {
			val ext = e.getActionCommand

			if (extRegex.pattern.matcher(ext).matches()) {
				listModel.addExtension(ext)
				extField.setText("")
			}
		}
	})


	{
		val scrollPane = new JScrollPane(extList)
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)

		setLayout(new BorderLayout)
		add(scrollPane, BorderLayout.CENTER)
		add(extField, BorderLayout.SOUTH)
	}

	/**
	  * Shows a dialog box, where file extensions can be added to a predefined list and chosen
	  * @param parent The dialog box parent
	  */
	def display(parent: Component) = {
		val choiceBackup = extList.getSelectedIndices

		val buttonPressed = JOptionPane.showOptionDialog(parent, this, "File Type Chooser",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null)

		// restore old choices
		if (buttonPressed != JOptionPane.OK_OPTION)
			extList.setSelectedIndices(choiceBackup)
	}

	def getSelectedExtensions = extList.getSelectedValuesList.toList

	def getSelectedExtensionsCount = extList.getSelectedValuesList.size

	class FileExtensionListModel extends AbstractListModel[String] {

		private val elements = new ListBuffer[String]
		elements.addAll(Config.getStringList("default-file-extensions"))

		def addExtension(ext: String) = {
			elements.add(ext)
			fireContentsChanged(this, elements.size, elements.size)
		}

		override def getElementAt(index: Int): String = elements(index)

		override def getSize: Int = elements.size
	}

	class FileExtensionListRenderer extends DefaultListCellRenderer {
		setHorizontalAlignment(SwingConstants.CENTER)
	}


}
