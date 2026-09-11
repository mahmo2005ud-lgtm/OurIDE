package com.itsaky.androidide.build.config

import org.gradle.api.JavaVersion

/** Build configuration for the IDE. */
object BuildConfig {
  const val packageName = "com.itsaky.androidide"
  const val compileSdk = 34
  const val minSdk = 26
  const val targetSdk = 28
  const val ndkVersion = "26.1.10909125"
  val javaVersion = JavaVersion.VERSION_11
}
