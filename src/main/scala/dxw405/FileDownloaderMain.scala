package dxw405

import dxw405.gui.FileDownloaderGUI
import dxw405.util.{Config, Logging}

class FileDownloaderMain {
	def init(): Unit = {
		Logging.debug("Logger initiated")

		if (!Config.check()) {
			Logging.error("Could not load config, exiting")
			return
		}

		val model = new DownloaderModel()
		val gui = new FileDownloaderGUI(model)

		gui.show()
	}

}

object FileDownloaderMain {
	def main(args: Array[String]) = new FileDownloaderMain().init()
}
