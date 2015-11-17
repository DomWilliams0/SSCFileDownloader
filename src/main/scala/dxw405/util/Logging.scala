package dxw405.util

import java.io.{PrintWriter, StringWriter}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object Logging {
	private val logger = Logger(LoggerFactory.getLogger("FileDownloader"))

	def error(message: String): Unit = logger.error(message)

	private def stackTrace(exception: Exception): Unit = {
		val wrapper = "------------------"
		val sw = new StringWriter
		val pw = new PrintWriter(sw)
		exception.printStackTrace(pw)
		debug(s"Stack trace: \n$wrapper\n$sw\n$wrapper")
	}

	def error(message: String, exception: Exception): Unit = {
		logger.error(s"$message (${exception.getMessage})")
		stackTrace(exception)
	}

	def warn(message: String): Unit = logger.warn(message)

	def warn(message: String, exception: Exception): Unit = {
		logger.warn(s"$message (${exception.getMessage})")
		stackTrace(exception)
	}

	def info(message: String): Unit = logger.info(message)

	def debug(message: String): Unit = logger.debug(message)

	def trace(message: String): Unit = logger.trace(message)
}