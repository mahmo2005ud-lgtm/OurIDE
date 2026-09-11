package com.itsaky.androidide.build.config

import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

const val KEY_ALIAS = "IDE_SIGNING_ALIAS"
const val AUTH_PASS = "IDE_SIGNING_AUTH_PASS"
const val AUTH_USER = "IDE_SIGNING_AUTH_USER"
const val KEY_PASS = "IDE_SIGNING_KEY_PASS"
const val KEY_STORE_PASS = "IDE_SIGNING_STORE_PASS"
const val KEY_URL = "IDE_SIGNING_URL"
const val KEY_BIN = "IDE_SIGNING_KEY_BIN"

const val AGP_VERSION_MINIMUM = "7.2.0"

val Project.signingKey: Provider<RegularFile>
  get() = rootProject.layout.buildDirectory.file("signing/signing-key.jks")
