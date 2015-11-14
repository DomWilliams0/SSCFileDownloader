package dxw405.gui;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class InputPanel extends JPanel
{
	private JTextField siteField;
	private JFileChooser saveDirChooser;

	public InputPanel()
	{
		siteField = new JTextField();
		saveDirChooser = new JFileChooser();

		// init fields
		saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		saveDirChooser.setMultiSelectionEnabled(false);

		setLayout(new BorderLayout());

		// site field
		JPanel sitePanel = new JPanel();
		sitePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		sitePanel.setLayout(new BoxLayout(sitePanel, BoxLayout.X_AXIS));

		sitePanel.add(new JLabel("Webpage"));
		sitePanel.add(Box.createHorizontalStrut(10));

		sitePanel.add(siteField);
		add(sitePanel, BorderLayout.NORTH);
	}


}
