package dxw405.gui

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, ActionListener}
import java.io.File
import java.nio.file.{InvalidPathException, Paths}
import javax.swing._
import javax.swing.border.EmptyBorder

import dxw405.DownloaderModel
import dxw405.util.{Config, Logging}

class InputPanel(downloaderModel: DownloaderModel) extends JPanel {
  private final val borderThickness = 5
  private final val defaultDir: File = new File(Config.getString("default-save-dir").replace("$HOME", sys.env("HOME"))).getCanonicalFile
  Logging.debug(f"Default save directory is set to ${defaultDir.getAbsolutePath}")

  private val model = downloaderModel

  private val saveDirField = new JTextField
  private val siteField = new TextFieldPlaceholder("http://google.com")
  private val saveDirChooser = new JFileChooser(defaultDir)

  saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
  saveDirChooser.setMultiSelectionEnabled(false)

  // init panel
  val fieldContainer = createBoxPanel(BoxLayout.Y_AXIS)
  fieldContainer.add(createField("Webpage", siteField), BorderLayout.NORTH)
  fieldContainer.add(createField("Save Directory", createSaveDirChooserPanel))

  setLayout(new BorderLayout)
  add(fieldContainer, BorderLayout.NORTH)

  /**
    * Helper method to create a JPanel with a BoxLayout along the given axis
    *
    * @param axis The axis
    * @return A new panel
    */
  private def createBoxPanel(axis: Int) = {
    val panel = new JPanel
    panel.setLayout(new BoxLayout(panel, axis))
    panel
  }

  /**
    * Creates a field that fills the panel horizontally
    *
    * @param label     The component's label
    * @param component The component
    * @return A panel containing the label and the component
    */
  private def createField(label: String, component: JComponent): JPanel = {
    val panel: JPanel = createBoxPanel(BoxLayout.X_AXIS)
    panel.setBorder(new EmptyBorder(borderThickness, borderThickness, borderThickness, borderThickness))
    panel.add(new JLabel(label))
    addSpacing(panel)
    panel.add(component)
    panel
  }

  /**
    * Creates the input fields for choosing save directory
    *
    * @return The panel holding the fields
    */
  private def createSaveDirChooserPanel: JPanel = {
    val panel: JPanel = createBoxPanel(BoxLayout.X_AXIS)
    saveDirField.setText(defaultDir.getAbsolutePath)
    saveDirChooser.setCurrentDirectory(defaultDir)
    val chooseButton = new JButton("Choose")
    chooseButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        try {
          // update from text input
          val p = Paths.get(saveDirField.getText())
          if (!p.toFile.exists())
            throw new InvalidPathException("", "")

          saveDirChooser.setSelectedFile(p.toFile)
        } catch {
          case e: InvalidPathException => saveDirChooser.setSelectedFile(defaultDir)
        }

        val choice = saveDirChooser.showDialog(InputPanel.this, "Select")
        if (choice == JFileChooser.APPROVE_OPTION && saveDirChooser.getSelectedFile != null)
          saveDirField.setText(saveDirChooser.getSelectedFile.getAbsolutePath)
      }
    })

    panel.add(saveDirField)
    addSpacing(panel)
    panel.add(chooseButton)
    panel

  }

  /**
    * Adds a consistent amount of horizontal spacing to the given component
    *
    * @param component The component to add spacing to
    */
  private def addSpacing(component: JComponent) {
    component.add(Box.createHorizontalStrut(10))
  }

  def downloadClicked() {
    val selectedFile: File = Option(saveDirChooser.getSelectedFile).getOrElse(defaultDir)

    // create file
    if (!selectedFile.exists()) {
      val create = JOptionPane.showOptionDialog(this, f"'${selectedFile.getPath}' does not exist.\nWould you like to create it?",
        "Uh oh", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null)

      if (create != JOptionPane.YES_OPTION)
        return

      selectedFile.mkdir()
    }


    val error = model.download(siteField.getText, selectedFile.getAbsolutePath)
    if (error.isDefined)
      JOptionPane.showMessageDialog(this, f"<html><b>Could not download files</b><br>${error.get}</html>",
        "Uh oh", JOptionPane.ERROR_MESSAGE)
  }

}
