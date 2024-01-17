package com.paranid5.crossword_generator.data

import com.paranid5.crossword_generator.data.app.{AppConfigChannel, SessionChannel}
import com.paranid5.crossword_generator.data.app.navigation.NavigationService
import com.paranid5.crossword_generator.data.storage.StoragePreferences

/** All layers of the application */

type FullEnvironment = StoragePreferences & SessionChannel & AppConfigChannel & NavigationService
