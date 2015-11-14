package dxw405.gui;

import dxw405.DownloaderModel;
import dxw405.Utils;
import dxw405.util.Logging;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ImageDownloaderGUI implements ActionListener
{
	private DownloaderModel model;
	private JFrame frame;
	private InputPanel inputPanel;
	private TopPanel topPanel;

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

		inputPanel = new InputPanel(model);
		topPanel = new TopPanel(this);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(inputPanel, BorderLayout.SOUTH);
		headerPanel.add(topPanel, BorderLayout.NORTH);
		frame.add(headerPanel, BorderLayout.NORTH);
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Action a = Utils.parseEnum(Action.class, e.getActionCommand(), null, false);
		if (a == null)
			return;


		switch (a)
		{
			case DOWNLOAD_BUTTON_PRESSED:
				inputPanel.downloadClicked();
				break;
			case EXIT:
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				break;
		}
	}
}
