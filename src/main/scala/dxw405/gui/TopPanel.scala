package dxw405.gui

import java.awt.{FlowLayout, BorderLayout}
import java.awt.event.ActionListener
import javax.swing.{JButton, JMenuBar, JPanel}

import dxw405.gui.GUIAction.GUIAction

class TopPanel(buttonListener: ActionListener) extends JMenuBar {
  {
    setLayout(new BorderLayout())

    val buttons = new JPanel(new FlowLayout(FlowLayout.LEFT))
    buttons.add(addButton(buttonListener, "Download", GUIAction.DownloadButtonPressed))
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
