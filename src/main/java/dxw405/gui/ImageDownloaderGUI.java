package dxw405.gui;

import dxw405.DownloaderModel;
import dxw405.util.Logging;

import javax.swing.*;
import java.awt.BorderLayout;

public class ImageDownloaderGUI
{
	private DownloaderModel model;
	private JFrame frame;

	public ImageDownloaderGUI(DownloaderModel model)
	{
		this.model = model;
		this.frame = new JFrame();

		initFrame();
	}

	private void initFrame()
	{
		initOSSkin();

		frame.setLayout(new BorderLayout());
		frame.setTitle("Image Downloader");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new InputPanel(), BorderLayout.NORTH);
	}

	private void initOSSkin()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			Logging.error("Could not use native theme", e);
		}
	}

	public void show()
	{
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
