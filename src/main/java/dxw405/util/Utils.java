package dxw405.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.EnumSet;

/**
 * Reused from https://github.com/DomWilliams0/SSCEmailClient
 */
public class Utils
{
	private static final String[] FILESIZE_UNITS = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
	private static final DecimalFormat FILESIZE_FORMAT = new DecimalFormat("#,##0.#");

	private Utils()
	{
	}

	/**
	 * Opens the given file for reading
	 *
	 * @param file The file to read
	 * @return The file stream, or null if the operation failed
	 */
	public static FileInputStream readFile(File file)
	{
		if (!validateFile(file))
			return null;

		FileInputStream stream;
		try
		{
			stream = new FileInputStream(file);
			return stream;
		} catch (IOException e)
		{
			Logging.error("Could not load file (" + file.getPath() + "): " + e);
			return null;
		}
	}

	/**
	 * Validates that the given file is not null and exists
	 *
	 * @param file The file to validate
	 * @return If the file is valid
	 */
	public static boolean validateFile(File file)
	{
		// null
		if (file == null)
		{
			Logging.error("Input file is null");
			return false;
		}

		// doesn't exist
		if (!file.exists())
		{
			Logging.error("Input file doesn't exist (" + file.getPath() + ")");
			return false;
		}

		return true;
	}

	/**
	 * Closes the given stream
	 *
	 * @param stream The stream to close
	 */
	public static void closeStream(InputStream stream)
	{
		try
		{
			stream.close();
		} catch (IOException e)
		{
			Logging.error("Cannot close stream: " + e);
		}
	}

	/**
	 * Capitalises every word in the given string
	 *
	 * @param sentence The sentence to capitalise
	 * @return The first letter of every word capitalised, the rest lowercase
	 */
	public static String capitalise(String sentence)
	{
		if (sentence == null)
			return null;
		if (sentence.isEmpty())
			return sentence;

		String[] split = sentence.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : split)
		{
			int length = s.length();
			switch (length)
			{
				case 0:
					sb.append(" ");
					break;
				case 1:
					sb.append(s.toUpperCase()).append(" ");
					break;
				default:
					sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase()).append(" ");
					break;
			}
		}

		return sb.toString().trim();
	}

	/**
	 * Converts a String into an Enum
	 *
	 * @param enumClass    The enum
	 * @param s            The string to parse
	 * @param defaultValue The default value to return if the given string is invalid
	 * @return The corresponding enum value, or null if none was found
	 */
	public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String s, E defaultValue)
	{
		return parseEnum(enumClass, s, defaultValue, true);
	}

	/**
	 * Converts a String into an Enum
	 *
	 * @param enumClass      The enum
	 * @param s              The string to parse
	 * @param convertToUpper If the given string should be converted to uppercase before comparison
	 * @param defaultValue   The default value to return if the given string is invalid
	 * @return The corresponding enum value, or null if none was found
	 */
	public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String s, E defaultValue, boolean convertToUpper)
	{
		EnumSet<E> values = EnumSet.allOf(enumClass);
		String sCompare = convertToUpper ? s.toUpperCase() : s;
		for (E value : values)
			if (value.toString().equals(sCompare))
				return value;
		return defaultValue;
	}

	/**
	 * Gets a file's size in the appropriate units
	 * Taken from stackoverflow: http://stackoverflow.com/a/5599842
	 *
	 * @param file The file
	 * @return The file's size in a readable format
	 */
	public static String readableFileSize(File file)
	{
		return readableFileSize(file.length());
	}

	/**
	 * Gets a file's size in the appropriate units
	 * Taken from stackoverflow: http://stackoverflow.com/a/5599842
	 *
	 * @param size The file size to convert
	 * @return The given size in a readable format
	 */
	public static String readableFileSize(long size)
	{
		if (size <= 0) return "0";
		int digitGroups = (int) (Math.log10(size) / Math.log10(1000));
		return FILESIZE_FORMAT.format(size / Math.pow(1000, digitGroups)) + " " + FILESIZE_UNITS[digitGroups];
	}


}
