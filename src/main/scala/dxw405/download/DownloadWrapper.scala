package dxw405.download

import java.io.File
import java.net.URL

class DownloadWrapper(url: URL, saveDir: File) {

	var status: Status = StatusNotStarted

	def saveDirectory = saveDir

	def fileURL = url
}
