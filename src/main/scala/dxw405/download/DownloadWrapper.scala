package dxw405.download

import java.net.URL

class DownloadWrapper(url: URL) {

  val fileURL = url
  val status = StatusNotStarted

  override def toString = s"DownloadWrapper($fileURL, $status)"
}
