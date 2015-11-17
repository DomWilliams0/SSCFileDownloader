package dxw405.gui

import java.awt.event.ActionListener
import java.awt.{BorderLayout, FlowLayout}
import javax.swing.{JButton, JMenuBar, JPanel}

import dxw405.gui.GUIAction.GUIAction

class TopPanel(buttonListener: ActionListener) extends JMenuBar {

	private var _downloadButton: JButton = null

	def downloadButton = _downloadButton

	{
		setLayout(new BorderLayout())

		val buttons = new JPanel(new FlowLayout(FlowLayout.LEFT))
		_downloadButton = addButton(buttonListener, "Download", GUIAction.DownloadButtonPressed)
		buttons.add(_downloadButton)
		buttons.add(addButton(buttonListener, "Exit", GUIAction.Exit))
		add(buttons, BorderLayout.CENTER)
	}

	private def addButton(buttonListener: ActionListener, label: String, action: GUIAction): JButton = {
		val goButton = new JButton(label)
		goButton.setActionCommand(action.toString)
		goButton.addActionListener(buttonListener)
		goButton
	}
}
