package dxw405

import dxw405.gui.ImageDownloaderGUI
import dxw405.util.{Config, Logging}

class ImageDownloaderMain {
	def init(): Unit = {
		Logging.debug("Logger initiated")

		if (!Config.check()) {
			Logging.error("Could not load config, exiting")
			return
		}

		val model = new DownloaderModel()
		val gui = new ImageDownloaderGUI(model)

		gui.show()
	}

}

object ImageDownloaderMain {
	def main(args: Array[String]) = new ImageDownloaderMain().init()
}
