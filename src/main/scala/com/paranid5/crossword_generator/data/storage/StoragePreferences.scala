package com.paranid5.crossword_generator.data.storage

import com.paranid5.crossword_generator.data.utils.toRIO

import zio.channel.{Channel, foreverWhile}
import zio.stm.TReentrantLock
import zio.stream.{SubscriptionRef, UStream}
import zio.{RIO, ULayer, URIO, ZIO, ZLayer}

import java.io.{File, PrintWriter}

import scala.util.Using
import scala.xml.{Elem, Node, XML}

private val StoragePath = "preferences.xml"

/**
 * Accesses and manages the app's XML storage states
 *
 * @param dataRef     reference to current XML app config
 * @param updateChan  channel to broadcast XML app config changes
 * @param storageLock RW Lock of the storage access
 */

case class StoragePreferences(
  dataRef:     SubscriptionRef[Elem],
  updateChan:  Channel[Boolean],
  storageLock: TReentrantLock
)

/** Provides a layer for the [[StoragePreferences]] */

object StoragePreferences:
  /**
   * Creates a ZIO layer for the StoragePreferences
   * @return [[ULayer]] that provides the [[StoragePreferences]]
   */

  val layer: ULayer[StoragePreferences] =
    ZLayer:
      for
        dataRef    ← SubscriptionRef make loadStorageFile()
        updateChan ← Channel.make[Boolean]
        lock       ← TReentrantLock.make.commit
      yield StoragePreferences(dataRef, updateChan, lock)

/**
 * Provides [[StoragePreferences]] from the ZIO environment
 * @return [[StoragePreferences]] from the ZIO layers
 */

private def storagePreferences: URIO[StoragePreferences, StoragePreferences] =
  for env ← ZIO.service[StoragePreferences]
    yield env

/**
 * Retrieves the hot-reload state for the XML data
 * @return [[SubscriptionRef]] of the XML app config
 */

def dataRef: URIO[StoragePreferences, SubscriptionRef[Elem]] =
  for pref ← storagePreferences
    yield pref.dataRef

/**
 * Retrieves the ZIO stream of the XML data
 * @return [[UStream]] of the XML app config changes
 */

def dataStream: URIO[StoragePreferences, UStream[Elem]] =
  for ref ← dataRef
    yield ref.changes

/**
 * Retrieves the entire app config data as an XML element
 * @return [[Elem]] represents app config data
 */

def data: URIO[StoragePreferences, Elem] =
  for
    ref  ← dataRef
    elem ← ref.get
  yield elem

/**
 * Acquires RW Lock of the storage access
 * @return [[URIO]] with [[TReentrantLock]]
 */

def storageLock: URIO[StoragePreferences, TReentrantLock] =
  for pref ← storagePreferences
    yield pref.storageLock

/**
 * Updates the XML data by reloading it from the file
 * @return [[URIO]] that completes when state is updated
 */

private def updateData(): RIO[StoragePreferences, Unit] =
  for
    lock ← storageLock
    _ ← ZIO
      .scoped:
        for
          _       ← lock.readLock
          ref     ← dataRef
          updated ← ZIO attemptBlocking loadStorageFile()
          _       ← ref set updated
        yield ()
  yield ()

/**
 * Provides the channel for notifying about XML data changes
 * @return [[Channel]] itself
 */

def updateChannel(): URIO[StoragePreferences, Channel[Boolean]] =
  for pref ← storagePreferences
    yield pref.updateChan

/**
 * Continuously monitors states changes
 * and updates the XML data accordingly
 *
 * @return [[RIO]] that never completes,
 *         must be executed in the different fibber
 * @see [[ZIO.fork]]
 */

def monitorDataChanges(): RIO[StoragePreferences, Unit] =
  foreverWhile:
    for
      chan ← updateChannel()
      data ← chan.receive.toRIO
      _    ← updateData().fork
    yield data

/**
 * Sends a broadcast to listeners
 * that the XML data requires changes
 *
 * @return [[RIO]] that completes when
 *         the message is delivered
 */

def notifyDataUpdate(): RIO[StoragePreferences, Unit] =
  for
    chan ← updateChannel()
    _    ← (chan send true).toRIO
  yield ()

/**
 * Sends a broadcast to listeners
 * that the XML data requires changes
 *
 * @param chan update channel through
 *             which message has to be sent
 * @return [[RIO]] that completes when
 *         the message is delivered
 */

def notifyDataUpdate(chan: Channel[Boolean]): RIO[Any, Unit] =
  (chan send true).toRIO

/**
 * Provides a ZIO stream of the XML parsed value
 *
 * @param that      required XML label (tag or parameter)
 * @param default   used if no matching element is found
 * @param predicate checks if parsed value is correct.
 *                  If not, gets [[default]] value
 * @return string stream of parsed XML data
 */

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

/**
 * Provides a string from the XML parsed value
 *
 * @param that      required XML label (tag or parameter)
 * @param default   used if no matching element is found
 * @param predicate checks if parsed value is correct.
 *                  If not, gets [[default]] value
 * @return parsed XML data
 */

private def stringData(
  that:    String,
  default: String = "",
  predicate: String ⇒ Boolean = { _ ⇒ true }
): URIO[StoragePreferences, String] =
  data.map: data ⇒
    data firstOption that match
      case Some(value)
        if predicate(value.text) ⇒
        value.text

      case _ ⇒ default

/**
 * Retrieves the XML data from the file.
 * If the file does not exist,
 * it creates a new file with the default XML structure
 *
 * @return parsed XML data or the empty one
 */

private def loadStorageFile(): Elem =
  val file = File(StoragePath)

  if !file.exists() then
    file.createNewFile()

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

extension (elem: Elem)
  /**
   * Retrieves the first matching node
   *
   * @param that tag or parameter of the node
   * @return node or none if it wasn't found
   */

  private def firstOption(that: String): Option[Node] =
    (elem \ that).headOption

  /**
   * Retrieves the first matching node
   *
   * @param that tag or parameter of the node
   * @return node itself
   * @throws NoSuchElementException if node is not found
   */

  private def first(that: String): Node =
    (elem \ that).head