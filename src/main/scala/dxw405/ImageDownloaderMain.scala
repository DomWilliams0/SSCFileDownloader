package dxw405

import dxw405.gui.ImageDownloaderGUI
import dxw405.util.Logging

class ImageDownloaderMain
{
	def init(): Unit =
	{
		Logging.debug("Logger initiated")

		val model = new DownloaderModel()
		val gui = new ImageDownloaderGUI(model)

		gui.show()
	}

}

object ImageDownloaderMain
{
	def main(args: Array[String]) = new ImageDownloaderMain().init()
}
