package dxw405.download

import java.net.URL

class DownloadWrapper(url: URL) {

  var status: Status = StatusNotStarted

  private val _fileURL = url

  def fileURL = _fileURL
}
