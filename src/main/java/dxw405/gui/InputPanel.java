package dxw405.gui;


import dxw405.DownloaderModel;
import dxw405.util.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputPanel extends JPanel
{
	private static final int BORDER_THICKNESS = 5;
	private static final File DEFAULT_DIR;

	static
	{
		DEFAULT_DIR = Paths.get(System.getProperty("user.home"), "downloaded-files").toFile();
		Logging.debug("Set default save directory to '" + DEFAULT_DIR.getAbsolutePath() + "'");
	}

	private DownloaderModel model;

	private JTextField saveDirField;
	private JTextField siteField;
	private JFileChooser saveDirChooser;

	public InputPanel(DownloaderModel downloaderModel)
	{
		model = downloaderModel;
		siteField = new JTextField();
		saveDirField = new JTextField();
		saveDirChooser = new JFileChooser();

		// init fields
		saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		saveDirChooser.setMultiSelectionEnabled(false);

		setLayout(new BorderLayout());
		JPanel fieldContainer = createBoxPanel(BoxLayout.Y_AXIS);

		// webpage field
		fieldContainer.add(createField("Webpage", siteField), BorderLayout.NORTH);

		// save directory field
		fieldContainer.add(createField("Save Directory", createSaveDirChooserPanel()));

		add(fieldContainer, BorderLayout.NORTH);
	}

	/**
	 * Helper method to create a JPanel with a BoxLayout along the given axis
	 *
	 * @param axis The axis
	 * @return A new panel
	 */
	private JPanel createBoxPanel(int axis)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, axis));
		return panel;
	}

	/**
	 * Creates a field that fills the panel horizontally
	 *
	 * @param label     The component's label
	 * @param component The component
	 * @return A panel containing the label and the component
	 */
	private JPanel createField(String label, JComponent component)
	{
		JPanel panel = createBoxPanel(BoxLayout.X_AXIS);
		panel.setBorder(new EmptyBorder(BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS));

		panel.add(new JLabel(label));
		addSpacing(panel);

		panel.add(component);
		return panel;
	}

	/**
	 * Creates the input fields for choosing save directory
	 *
	 * @return The panel holding the fields
	 */
	private JPanel createSaveDirChooserPanel()
	{
		JPanel panel = createBoxPanel(BoxLayout.X_AXIS);

		// defaults
		saveDirField.setText(DEFAULT_DIR.getAbsolutePath());
		saveDirChooser.setCurrentDirectory(DEFAULT_DIR);

		JButton chooseButton = new JButton("Choose");
		chooseButton.addActionListener(e ->
		{
			try
			{
				// update from text input
				Path p = Paths.get(saveDirField.getText());
				if (!p.toFile().exists())
					throw new InvalidPathException("", "");

				saveDirChooser.setSelectedFile(p.toFile());

			} catch (InvalidPathException ex)
			{
				// invalid path; revert to home
				saveDirChooser.setSelectedFile(DEFAULT_DIR);
			}

			int choice = saveDirChooser.showDialog(this, "Select");
			if (choice != JFileChooser.APPROVE_OPTION)
				return;

			File selected = saveDirChooser.getSelectedFile();
			if (selected == null)
				return;

			saveDirField.setText(selected.getAbsolutePath());
		});

		panel.add(saveDirField);
		addSpacing(panel);
		panel.add(chooseButton);

		return panel;
	}

	/**
	 * Adds a consistent amount of horizontal spacing to the given component
	 *
	 * @param component The component to add spacing to
	 */
	private static void addSpacing(JComponent component)
	{
		component.add(Box.createHorizontalStrut(10));
	}


	public void downloadClicked()
	{
		File selectedFile = saveDirChooser.getSelectedFile();
		if (selectedFile == null)
			selectedFile = DEFAULT_DIR;

		model.download(siteField.getText(), selectedFile.getAbsolutePath());
	}
}
