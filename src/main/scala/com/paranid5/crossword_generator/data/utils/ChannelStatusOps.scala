package com.paranid5.crossword_generator.data.utils

import zio.{RIO, ZIO}
import zio.channel.ChannelStatus

extension[R, A] (chanIO: ZIO[R, ChannelStatus, A])
  /**
   * Transforms [[ChannelStatus]] to message in the exception
   * @return [[RIO]], where throwable is the [[ChannelStatus.toString]]
   */

  def toRIO: RIO[R, A] =
    chanIO mapError (e ⇒ Exception(e.toString))
