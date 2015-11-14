package dxw405.gui

import java.awt.BorderLayout
import java.awt.event.ActionListener
import javax.swing.{JButton, JLabel, JMenuBar, JPanel}

import dxw405.gui.GUIAction.GUIAction

class TopPanel(buttonListener: ActionListener) extends JMenuBar {
  {
    setLayout(new BorderLayout)
    add(new JLabel("  Specify and download files from a webpage!"), BorderLayout.WEST)

    val buttons = new JPanel
    buttons.add(addButton(buttonListener, "Download", GUIAction.DownloadButtonPressed))
    buttons.add(addButton(buttonListener, "Exit", GUIAction.Exit))
    add(buttons, BorderLayout.EAST)
  }

  private def addButton(buttonListener: ActionListener, label: String, action: GUIAction): JButton = {
    val goButton = new JButton(label)
    goButton.setActionCommand(action.toString)
    goButton.addActionListener(buttonListener)
    goButton
  }
}
