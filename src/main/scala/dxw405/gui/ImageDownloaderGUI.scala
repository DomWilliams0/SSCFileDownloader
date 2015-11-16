package dxw405.gui

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, ActionListener, WindowEvent}
import javax.swing.{JFrame, UIManager, WindowConstants}

import dxw405.DownloaderModel
import dxw405.util.{Config, Logging, Utils}

class ImageDownloaderGUI(model: DownloaderModel) extends ActionListener {
	if (Config.getBoolean("gui.native-skin"))
		initOSSkin()

	private val frame: JFrame = new JFrame()

	private val inputPanel: InputPanel = new InputPanel(model)
	private val topPanel: TopPanel = new TopPanel(this)
	private val taskList: TaskList = new TaskList(model)

	initFrame()

	private def initFrame() {
		frame.setLayout(new BorderLayout)
		frame.setTitle("Image Downloader")
		frame.setSize(Config.getInt("gui.width"), Config.getInt("gui.height"))
		frame.setResizable(false)
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

		// add components
		frame.add(inputPanel, BorderLayout.NORTH)
		frame.add(taskList, BorderLayout.CENTER)
		frame.setJMenuBar(topPanel)

		// model observers
		model.addObserver(taskList)
	}

	private def initOSSkin() {
		try {
			val theme = UIManager.getSystemLookAndFeelClassName
			UIManager.setLookAndFeel(theme)
			Logging.debug(s"Set theme to native ($theme)")
		}
		catch {
			case e: Exception =>
				Logging.error("Could not use native theme", e)
		}
	}

	def show() {
		frame.setLocationRelativeTo(null)
		frame.setVisible(true)
	}

	override def actionPerformed(e: ActionEvent): Unit = {
		val a = Utils.parseEnum(GUIAction, e.getActionCommand)

		a match {
			case Some(GUIAction.DownloadButtonPressed) => inputPanel.downloadClicked(Some(taskList))
			case Some(GUIAction.Exit) => frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
			case _ =>
		}
	}


}
