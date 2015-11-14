package dxw405

import dxw405.gui.ImageDownloaderGUI

class ImageDownloaderMain
{
	def init(): Unit =
	{
		val model = new DownloaderModel()
		val gui = new ImageDownloaderGUI(model)

		gui.show()
	}

}

object ImageDownloaderMain
{
	def main(args: Array[String]) = new ImageDownloaderMain().init()
}
