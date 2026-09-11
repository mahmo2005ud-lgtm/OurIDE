# OurIDE Build Reference

This file is a permanent reference for the current OurIDE Codemagic build strategy.

## Repository
- Project: OurIDE
- Branch: main
- Source archive: OurIDE_SOURCE_CLEAN_1.0.1.zip.part-00 + part-01

## Verified Codemagic environment
- Instance: mac_mini_m2
- Java: 17
- Gradle: 8.14.1
- Gradle embedded Kotlin: 2.0.21

## Important build rules
1. Do not use the failed Kotlin `.kts` init-script compatibility approach.
2. Do not force Kotlin 1.9/K1 on `kotlin-dsl` modules.
3. `kotlin-dsl` modules must use Gradle's embedded Kotlin version.
4. Legacy `kotlinOptions` in restored source should be migrated to `compilerOptions` during the build.
5. The build currently substitutes AGP 9.4.0 -> 8.13.2 and KSP 1.9.24-1.0.20 -> 2.3.12 during the Codemagic build.
6. Always inspect the first real Gradle error before changing the build strategy.
7. Never declare the APK build successful until `:core:app:assembleDebug` completes successfully and the APK artifact exists.

## Latest verified failure and conclusion
Gradle 8.14.1 reports that the build-logic `kotlin-dsl` modules are using Kotlin Gradle Plugin 2.3.21 while Gradle embeds Kotlin 2.0.21. It also reports language/API 1.8 as unsupported by the requested compiler. The correct direction is to remove explicit Kotlin JVM usage from `kotlin-dsl` convention/precompiled modules and let `kotlin-dsl` provide the embedded Kotlin version.

## Current Codemagic strategy
- Restore the split source archive.
- Configure Android SDK through local.properties.
- Migrate legacy Kotlin compiler configuration in build.gradle.kts files.
- Remove explicit kotlin-jvm plugin lines from files that use kotlin-dsl.
- Keep AGP/KSP compatibility substitutions.
- Run Gradle 8.14.1 directly with `:core:app:assembleDebug`.

The authoritative build workflow is `codemagic.yaml` on the main branch. This document is a persistent decision log so future fixes do not revert to already-failed approaches.
