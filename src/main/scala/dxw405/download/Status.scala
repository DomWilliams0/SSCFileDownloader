package dxw405.download

import java.awt.Color
import javax.swing.UIManager

sealed abstract class Status(prettyString: String, statusColour: Color) {
  private val _pretty = prettyString.toUpperCase
  private val _colour = statusColour

  def pretty = _pretty

  def colour = _colour

}

case object StatusNotStarted extends Status("Waiting", UIManager.getColor("Panel.background"))

case object StatusInProgress extends Status("Downloading", new Color(243, 156, 18))

case object StatusFailed extends Status("Failed", new Color(231, 76, 60))

case object StatusSucceeded extends Status("Success", new Color(46, 204, 113))