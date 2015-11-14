package dxw405.util

import java.io.{File, FileInputStream, IOException, InputStream}
import java.text.DecimalFormat

/**
  * Adapted from https://github.com/DomWilliams0/SSCEmailClient
  */
object Utils
{
	private val fileSizeUnits: Array[String] = Array[String]("B", "KB", "MB", "GB", "TB", "PB", "EB")
	private val fileSizeFormat: DecimalFormat = new DecimalFormat("#,##0.#")

	/**
	  * Opens the given file for reading
	  *
	  * @param file The file to read
	  * @return Some file stream, or None if the operation failed
	  */
	def readFile(file: File): Option[FileInputStream] =
	{
		if (!validateFile(file)) return None
		try
		{
			Some(new FileInputStream(file))
		}
		catch
			{
				case e: IOException =>
					Logging.error("Could not load file (" + file.getPath + "): " + e)
					None
			}
	}

	/**
	  * Validates that the given file is not null and exists
	  *
	  * @param file The file to validate
	  * @return If the file is valid
	  */
	def validateFile(file: File): Boolean =
	{
		if (file == null)
		{
			Logging.error("Input file is null")
			return false
		}
		if (!file.exists)
		{
			Logging.error("Input file doesn't exist (" + file.getPath + ")")
			return false
		}
		true
	}

	/**
	  * Closes the given stream
	  *
	  * @param stream The stream to close
	  */
	def closeStream(stream: InputStream)
	{
		try
		{
			if (stream != null)
				stream.close()
		}
		catch
			{
				case e: IOException =>
					Logging.error("Cannot close stream: " + e)
			}
	}

	/**
	  * Capitalises every word in the given string
	  *
	  * @param sentence The sentence to capitalise
	  * @return The first letter of every word capitalised, the rest lowercase
	  */
	def capitalise(sentence: String): String =
	{
		if (sentence == null) return null
		if (sentence.isEmpty) return sentence
		val split: Array[String] = sentence.split(" ")
		val sb: StringBuilder = new StringBuilder
		for (s <- split)
		{
			val length: Int = s.length
			length match
			{
				case 0 =>
					sb.append(" ")
				case 1 =>
					sb.append(s.toUpperCase).append(" ")
				case _ =>
					sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase).append(" ")
			}
		}
		sb.toString.trim
	}

	/**
	  * Converts a String into an Enumeration
	  * @param enum The enum
	  * @param s The string to parse
	  * @return Some Enumeration, or None if not found
	  */
	def parseEnum(enum: Enumeration, s: String): Option[enum.Value] =
		enum.values.find(_.toString == s)

	/**
	  * Gets a file's size in the appropriate units
	  * Taken from stackoverflow: http://stackoverflow.com/a/5599842
	  *
	  * @param file The file
	  * @return The file's size in a readable format
	  */
	def readableFileSize(file: File): String = readableFileSize(file.length)

	/**
	  * Gets a file's size in the appropriate units
	  * Taken from stackoverflow: http://stackoverflow.com/a/5599842
	  *
	  * @param size The file size to convert
	  * @return The given size in a readable format
	  */
	def readableFileSize(size: Long): String =
	{
		if (size <= 0) return "0"
		val digitGroups: Int = (Math.log10(size) / Math.log10(1000)).toInt
		fileSizeFormat.format(size / Math.pow(1000, digitGroups)) + " " + fileSizeUnits(digitGroups)
	}
}