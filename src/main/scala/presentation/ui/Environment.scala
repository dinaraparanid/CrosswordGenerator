package presentation.ui

import data.app.AppConfig
import zio.ZIO

def appTheme(): ZIO[AppConfig, Nothing, Theme] =
  for {
    env ← ZIO.environment[AppConfig]
    config = env.get[AppConfig]
    theme = config.theme
  } yield theme
