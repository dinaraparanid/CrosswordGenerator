package data.app

import data.storage.toRIO
import zio.channel.Channel
import zio.{RIO, ULayer, ZLayer}

final class SessionBroadcast(
  val updPageChan: Channel[Boolean]
):
  def updatePage(): RIO[Any, Unit] =
    (updPageChan send true).toRIO

object SessionBroadcast:
  val layer: ULayer[SessionBroadcast] =
    ZLayer:
      for pageChan ← Channel.make[Boolean] 
        yield SessionBroadcast(pageChan)
