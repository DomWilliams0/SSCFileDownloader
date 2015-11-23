package dxw405.gui

import java.awt.event.{ActionEvent, ActionListener}
import java.awt._
import java.io.File
import java.nio.file.{InvalidPathException, Paths}
import java.util
import javax.swing._
import javax.swing.border.EmptyBorder

import dxw405.DownloaderModel
import dxw405.gui.util.TextFieldPlaceholder
import dxw405.util.{Config, Logging}

class InputPanel(downloaderModel: DownloaderModel, downloadButton: JButton) extends JPanel {
	private final val borderThickness = 5
	private final val defaultDir: File = new File(Config.getString("default-save-dir").replace("$HOME", sys.env("HOME"))).getCanonicalFile
	Logging.debug(s"Default save directory is set to ${defaultDir.getAbsolutePath}")

	private val model = downloaderModel

	private val saveDirField = new JTextField
	private val siteField = new TextFieldPlaceholder("http://google.com")
	private val saveDirChooser = new JFileChooser(defaultDir)
	private val threadCount: JComboBox[Int] = new JComboBox[Int]()
	private val fileExtensions = new FileExtensionList

	saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
	saveDirChooser.setMultiSelectionEnabled(false)

	// init panel
	val fieldContainer = createBoxPanel(BoxLayout.Y_AXIS)
	fieldContainer.add(createField("Webpage", siteField), BorderLayout.NORTH)
	fieldContainer.add(createField("Save Directory", createSaveDirChooserPanel))

	fieldContainer.add(createDownloadOptionsPanel())

	setBorder(new EmptyBorder(5, 5, 0, 5))
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
	  * @return A panel holding the save directory input fields
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
	  * @return A panel with download threads count and file type input fields
	  */
	private def createDownloadOptionsPanel() = {
		def createThreadChoicePanel(): JPanel = {
			val vec = new util.Vector[Int]()
			Range(1, 40) foreach vec.add

			threadCount.setModel(new DefaultComboBoxModel[Int](vec))
			val field = createField("Download Threads", threadCount)

			val container = new JPanel(new FlowLayout(FlowLayout.CENTER))
			container.add(field)
			container
		}

		def createFileTypeChoice(): JComponent = {
			def label = s"Choose (${fileExtensions.getSelectedExtensionsCount})"
			val button = new JButton(new AbstractAction(label) {
				override def actionPerformed(e: ActionEvent): Unit = {
					fileExtensions.display(InputPanel.this)
					putValue(Action.NAME, label)
				}
			})

			val container = new JPanel(new FlowLayout(FlowLayout.CENTER))
			container.add(createField("File types", button))
			container
		}

		val panel = new JPanel(new GridLayout(1, 2))

		panel.add(createThreadChoicePanel())
		panel.add(createFileTypeChoice())

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

	def downloadClicked(taskList: Option[TaskList]) {
		val selectedFile: File = Option(saveDirChooser.getSelectedFile).getOrElse(defaultDir)

		// create file
		if (!selectedFile.exists()) {
			val create = JOptionPane.showOptionDialog(this, s"'${selectedFile.getPath}' does not exist.\nWould you like to create it?",
				"Uh oh", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null)

			if (create != JOptionPane.YES_OPTION)
				return

			selectedFile.mkdir()
		}


		// start download, and deal with errors
		new SwingWorker[Unit, Unit] {
			override def doInBackground(): Unit = {

				val error = model.download(siteField.getText,
					selectedFile.getAbsolutePath,
					threadCount.getSelectedItem.asInstanceOf[Int],
					fileExtensions.getSelectedExtensions,
					taskList,
					Some(downloadButton))
				if (error.isDefined)
					JOptionPane.showMessageDialog(InputPanel.this, s"<html><b>Could not download files</b><br>${error.get}</html>",
						"Uh oh", JOptionPane.ERROR_MESSAGE)
			}
		}.execute()

	}

}
