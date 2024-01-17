package com.paranid5.crossword_generator.data.app

import com.paranid5.crossword_generator.data.utils.toRIO

import zio.channel.Channel
import zio.{RIO, ULayer, ZLayer}

/**
 * Provides a service for broadcasting page updates
 * @param updPageChan the channel to receive page update requests
 */

final class SessionChannel(
  val updPageChan: Channel[Boolean]
) extends AnyVal:
  /**
   * Schedules an update to the crossword pdf page
   * @return [[RIO]] that will complete when the UI has been updated
   */

  def updatePage(): RIO[Any, Unit] =
    (updPageChan send true).toRIO

/** Provides a layer for the [[SessionBroadcast]] */

object SessionChannel:
  /**
   * Creates a ZIO layer for the SessionBroadcast
   * @return [[ULayer]] that provides the [[SessionBroadcast]]
   */

  lazy val layer: ULayer[SessionChannel] =
    ZLayer:
      for pageChan ← Channel.make[Boolean]
        yield SessionChannel(pageChan)
