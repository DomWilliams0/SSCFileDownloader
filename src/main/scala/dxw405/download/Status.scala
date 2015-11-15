package dxw405.download

import java.awt.Color
import javax.swing.UIManager

sealed abstract class Status(prettyString: String, statusColour: Color) {
  private val _pretty = prettyString
  private val _colour = statusColour

  def pretty = _pretty

  def colour = _colour

}

case object StatusNotStarted extends Status("Waiting", UIManager.getColor("Panel.background"))

case object StatusInProgress extends Status("Downloading", Color.BLUE)

case object StatusFailed extends Status("Pending", Color.RED)

case object StatusSucceeded extends Status("Success", Color.GREEN)