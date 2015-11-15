package dxw405.download

import java.net.URL
import java.util.concurrent.Future

class DownloadWrapper(url: URL) {

  var status: Status = StatusNotStarted
  var future: Future[Status] = null

  private val _fileURL = url

  def fileURL = _fileURL

  def cancel = future.cancel(true)

}
