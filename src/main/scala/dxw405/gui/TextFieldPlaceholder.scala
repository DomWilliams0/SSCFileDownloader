package dxw405.gui

import javax.swing._
import java.awt._

class TextFieldPlaceholder(placeHolderText:String) extends JTextField
{
	private val placeholder: String = placeHolderText

	override def paintComponent(g: Graphics)
	{
		super.paintComponent(g)
		if (getText.length == 0)
		{
			g.asInstanceOf[Graphics2D].setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
			val m: Int = 0xfefefefe
			g.setColor(new Color(((getBackground.getRGB & m) >>> 1) + ((getForeground.getRGB & m) >>> 1), true))
			g.drawString(placeholder, getInsets.left, getHeight / 2 + g.getFontMetrics.getAscent / 2 - 2)
		}
	}
}