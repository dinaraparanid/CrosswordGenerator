package presentation.ui

import presentation.ui.Themes.{Dark, Light}

import java.awt.Color

sealed trait Theme:
  def primaryColor: Color
  def secondaryColor: Color
  def secondaryAlternativeColor: Color
  def backgroundColor: Color
  def backgroundAlternativeColor: Color
  def fontColor: Color
  def enumValue: Themes

private case object LightTheme extends Theme:
  override def primaryColor: Color = grape

  override def secondaryColor: Color = russianViolet

  override def secondaryAlternativeColor: Color = columbiaBlue

  override def backgroundColor: Color = wisteria

  override def backgroundAlternativeColor: Color = amethyst

  override def fontColor: Color = Color.BLACK

  override def enumValue: Themes = Light

private case object DarkTheme extends Theme:
  override def primaryColor: Color = royalPurple

  override def secondaryColor: Color = pinkIavender

  override def secondaryAlternativeColor: Color = richBlack

  override def backgroundColor: Color = russianViolet

  override def backgroundAlternativeColor: Color = mauve

  override def fontColor: Color = Color.WHITE

  override def enumValue: Themes = Dark

enum Themes:
  case Light, Dark

def theme(themes: Themes): Theme =
  themes match
    case Themes.Light ⇒ LightTheme
    case Themes.Dark ⇒ DarkTheme

private def grape: Color = Color(111, 45, 189)
private def amethyst: Color = Color(166, 99, 204)
private def wisteria: Color = Color(178, 152, 220)
private def columbiaBlue: Color = Color(184, 208, 235)
private def celeste: Color = Color(185, 250, 248)


private def royalPurple: Color = Color(115, 83, 186)

private def mauve: Color = Color(250, 166, 255)

private def russianViolet: Color = Color(47, 25, 95)

private def richBlack: Color = Color(15, 16, 32)

private def pinkIavender: Color = Color(239, 195, 245)