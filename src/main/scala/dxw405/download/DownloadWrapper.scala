package dxw405.download

import java.net.URL

class DownloadWrapper(url: URL) {

	var status: Status = StatusNotStarted
	def fileURL = url
}
