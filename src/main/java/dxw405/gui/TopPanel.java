package dxw405.gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

public class TopPanel extends JMenuBar
{
	public TopPanel(ActionListener buttonListener)
	{
		setLayout(new BorderLayout());

		// title
		add(new JLabel("  Specify and download files from a webpage!"), BorderLayout.WEST);

		JPanel buttons = new JPanel();
		buttons.add(addButton(buttonListener, "Download", Action.DOWNLOAD_BUTTON_PRESSED));
		buttons.add(addButton(buttonListener, "Exit", Action.EXIT));
		add(buttons, BorderLayout.EAST);
	}

	private JButton addButton(ActionListener buttonListener, String label, Action action)
	{
		JButton goButton = new JButton(label);
		goButton.setActionCommand(action.toString());
		goButton.addActionListener(buttonListener);
		return goButton;
	}
}
