package presentation.ui

import java.awt.Color

sealed trait Theme:
  def primaryColor: Color
  def secondaryColor: Color
  def secondaryAlternativeColor: Color
  def backgroundColor: Color
  def backgroundAlternativeColor: Color
  def fontColor: Color

private case object LightTheme extends Theme:
  override def primaryColor: Color = royalPurple

  override def secondaryColor: Color = mauve

  override def secondaryAlternativeColor: Color = richBlack

  override def backgroundColor: Color = pinkIavender

  override def backgroundAlternativeColor: Color = russianViolet

  override def fontColor: Color = Color.BLACK

private case object DarkTheme extends Theme:
  override def primaryColor: Color = royalPurple

  override def secondaryColor: Color = richBlack

  override def secondaryAlternativeColor: Color = mauve

  override def backgroundColor: Color = russianViolet

  override def backgroundAlternativeColor: Color = pinkIavender

  override def fontColor: Color = Color.WHITE

enum Themes:
  case Light, Dark

def theme(themes: Themes): Theme =
  themes match
    case Themes.Light ⇒ LightTheme
    case Themes.Dark ⇒ DarkTheme

private def royalPurple: Color = Color(115, 83, 186)

private def mauve: Color = Color(250, 166, 255)

private def russianViolet: Color = Color(47, 25, 95)

private def richBlack: Color = Color(15, 16, 32)

private def pinkIavender: Color = Color(239, 195, 245)