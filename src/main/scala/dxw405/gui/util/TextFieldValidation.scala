package dxw405.gui.util

import java.awt.Color
import javax.swing.JTextField
import javax.swing.event.{DocumentEvent, DocumentListener}

object TextFieldValidation {

	private val invalid = new Color(231, 76, 60)

	def startValidation(textField: JTextField, validation: (String => Boolean)): Unit = {
		textField.getDocument.addDocumentListener(new DocumentListener {

			private val original = textField.getForeground

			private def check() =
				textField.setForeground(if (textField.getText.isEmpty || validation(textField.getText)) original else invalid)

			override def insertUpdate(e: DocumentEvent): Unit = check

			override def changedUpdate(e: DocumentEvent): Unit = check

			override def removeUpdate(e: DocumentEvent): Unit = check
		})
	}
}