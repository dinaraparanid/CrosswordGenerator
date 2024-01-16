package com.paranid5.crossword_generator.data.storage

import zio.channel.{Channel, ChannelStatus, foreverWhile}
import zio.stream.{SubscriptionRef, UStream}
import zio.{RIO, ULayer, URIO, ZIO, ZLayer}

import java.io.{File, PrintWriter}

import scala.xml.{Elem, Node, XML}
import scala.util.Using

private val StoragePath = "preferences.xml"

case class StoragePreferences(
  dataRef:    SubscriptionRef[Elem],
  updateChan: Channel[Boolean],
)

object StoragePreferences:
  val layer: ULayer[StoragePreferences] =
    ZLayer:
      for
        dataRef    ← SubscriptionRef make loadStorageFile()
        updateChan ← Channel.make[Boolean]
      yield StoragePreferences(dataRef, updateChan)

private def storagePreferences: URIO[StoragePreferences, StoragePreferences] =
  for env ← ZIO.service[StoragePreferences]
    yield env

def dataRef: URIO[StoragePreferences, SubscriptionRef[Elem]] =
  for pref ← storagePreferences
    yield pref.dataRef

def dataStream: URIO[StoragePreferences, UStream[Elem]] =
  for ref ← dataRef
    yield ref.changes

def data: URIO[StoragePreferences, Elem] =
  for
    ref ← dataRef
    elem ← ref.get
  yield elem

private def updateData(): URIO[StoragePreferences, Unit] =
  for
    ref ← dataRef
    _   ← ref set loadStorageFile()
  yield ()

def updateChannel(): URIO[StoragePreferences, Channel[Boolean]] =
  for pref ← storagePreferences
    yield pref.updateChan

def monitorDataChanges(): RIO[StoragePreferences, Unit] =
  foreverWhile:
    for
      chan ← updateChannel()
      data ← chan.receive
      _    ← updateData()
    yield data
  .toRIO

def notifyDataUpdate(): RIO[StoragePreferences, Unit] =
  for
    chan ← updateChannel()
    _    ← (chan send true).toRIO
  yield ()

def notifyDataUpdate(chan: Channel[Boolean]): RIO[Any, Unit] =
  for _ ← (chan send true).toRIO yield ()

private def stringDataStream(
  that:    String,
  default: String = "",
  predicate: String ⇒ Boolean = { _ ⇒ true }
): URIO[StoragePreferences, UStream[String]] =
  dataStream.map: data ⇒
    data
      .map(_ firstOption that)
      .map:
        case Some(value)
          if predicate(value.text) ⇒
          value.text

        case _ ⇒ default

private def stringData(
  that:    String,
  default: String = "",
  predicate: String ⇒ Boolean = { _ ⇒ true }
): URIO[StoragePreferences, String] =
  data.map: data ⇒
    data.firstOption(that) match
      case Some(value)
        if predicate(value.text) ⇒
        value.text

      case _ ⇒ default

private def loadStorageFile(): Elem =
  val file = File(StoragePath)

  if file.createNewFile() then
    Using(PrintWriter(file)):
      _ println
      """<data>
        |    <theme/>
        |    <font/>
        |    <title/>
        |    <words/>
        |    <document/>
        |</data>""".stripMargin

  XML loadFile StoragePath

extension[R, A] (chanIO: ZIO[R, ChannelStatus, A])
  def toRIO: RIO[R, A] =
    chanIO mapError (e ⇒ Exception(e.toString))

extension (elem: Elem)
  private def firstOption(that: String): Option[Node] =
    (elem \ that).headOption

  private def first(that: String): Node =
    (elem \ that).head