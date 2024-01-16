package com.paranid5.crossword_generator.data

import com.paranid5.crossword_generator.data.app.{AppBroadcast, SessionBroadcast}
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.data.storage.StoragePreferences

type FullEnvironment = StoragePreferences & SessionBroadcast & AppBroadcast & NavigationService
