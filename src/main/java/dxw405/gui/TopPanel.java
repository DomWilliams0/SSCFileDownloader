package dxw405.gui;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel
{
	public TopPanel(ActionListener buttonListener)
	{
		setLayout(new FlowLayout(FlowLayout.RIGHT));

		JPanel container = new JPanel();
		container.add(addButton(buttonListener, "Download", Action.DOWNLOAD_BUTTON_PRESSED));
		container.add(addButton(buttonListener, "Exit", Action.EXIT));

		add(container);
	}

	private JButton addButton(ActionListener buttonListener, String label, Action action)
	{
		JButton goButton = new JButton(label);
		goButton.setActionCommand(action.toString());
		goButton.addActionListener(buttonListener);
		return goButton;
	}
}
