package dxw405.util

import java.util

import com.typesafe.config.{ConfigException, ConfigFactory, ConfigValue}

object Config {
  private lazy val config = ConfigFactory.load()

  /**
    * Checks the config against the reference
    * @return If the config was loaded properly and is valid
    */
  def check(): Boolean = {
    try {
      config.checkValid(ConfigFactory.defaultReference())
      true
    } catch {
      case e: ConfigException =>
        Logging.error("Failed to load config", e)
        false
    }
  }

  Logging.debug("Config loaded")

  def getBoolean(path: String): Boolean = config.getBoolean(path)

  def getString(path: String): String = config.getString(path)

  def getInt(path: String): Int = config.getInt(path)

  def getDouble(path: String): Double = config.getDouble(path)

  def getValue(path: String): ConfigValue = config.getValue(path)

  def getStringList(path: String): util.List[String] = config.getStringList(path)
}
