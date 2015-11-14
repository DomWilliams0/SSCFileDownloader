package dxw405.gui

import javax.swing.JFrame

import dxw405.DownloaderModel


class ImageDownloaderGUI(downloaderModel: DownloaderModel)
{
	private val model = downloaderModel
	private val frame = new JFrame("Image Downloader")

	// init gui
	frame.setSize(800, 600)
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

	def show(): Unit =
	{
		frame.setLocationRelativeTo(null)
		frame.setVisible(true)
	}


}
