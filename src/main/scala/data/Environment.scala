package data

import data.app.{AppBroadcast, SessionBroadcast}
import data.app.navigation.NavigationService
import data.storage.StoragePreferences

type FullEnvironment = StoragePreferences & SessionBroadcast & AppBroadcast & NavigationService
