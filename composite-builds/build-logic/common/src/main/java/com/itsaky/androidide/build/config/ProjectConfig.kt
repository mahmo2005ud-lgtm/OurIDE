package com.itsaky.androidide.build.config

import org.gradle.api.Project

object ProjectConfig {
  const val REPO_HOST = "github.com"
  const val REPO_OWNER = "AndroidIDEOfficial"
  const val REPO_NAME = "AndroidIDE"
  const val REPO_URL = "https://$REPO_HOST/$REPO_OWNER/$REPO_NAME"
  const val SCM_GIT = "scm:git:git://$REPO_HOST/$REPO_OWNER/$REPO_NAME.git"
  const val SCM_SSH = "scm:git:ssh://git@$REPO_HOST/$REPO_OWNER/$REPO_NAME.git"
  const val PROJECT_SITE = "https://m.androidide.com"
}

private var shouldPrintNotAGitRepoWarning = true
private var shouldPrintVersionName = true

val Project.isFDroidBuild: Boolean
  get() {
    if (!FDroidConfig.hasRead) FDroidConfig.load(this)
    return FDroidConfig.isFDroidBuild
  }

val Project.simpleVersionName: String
  get() {
    if (!CI.isGitRepo) {
      if (shouldPrintNotAGitRepoWarning) {
        logger.warn("Unable to infer version name. The build is not running on a git repository.")
        shouldPrintNotAGitRepoWarning = false
      }
      return "1.0.0-beta"
    }

    val version = rootProject.version.toString()
    val regex = Regex("^v\\d+\\.?\\d+\\.?\\d+-\\w+")
    val simpleVersion = regex.find(version)?.value?.substring(1)?.also {
      if (shouldPrintVersionName) {
        logger.warn("Simple version name is '$it' (from version $version)")
        shouldPrintVersionName = false
      }
    }

    if (simpleVersion == null) {
      if (CI.isTestEnv) return "1.0.0-beta"
      throw IllegalStateException("Cannot extract simple version name. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix")
    }
    return simpleVersion
  }

private var shouldPrintVersionCode = true
val Project.projectVersionCode: Int
  get() {
    val version = simpleVersionName
    val regex = Regex("^\\d+\\.?\\d+\\.?\\d+")
    val versionCode = regex.find(version)?.value?.replace(".", "")?.toInt()?.also {
      if (shouldPrintVersionCode) {
        logger.warn("Version code is '$it' (from version ${version}).")
        shouldPrintVersionCode = false
      }
    } ?: throw IllegalStateException("Cannot extract version code. Invalid version string '$version'. Version names must be SEMVER with 'v' prefix")
    return versionCode
  }

val Project.publishingVersion: String
  get() {
    var publishing = simpleVersionName
    if (isFDroidBuild) return publishing
    if (CI.isCiBuild && CI.isGitRepo && CI.branchName != "main") {
      publishing += "-${CI.commitHash}-SNAPSHOT"
    }
    return publishing
  }

val Project.downloadVersion: String
  get() = if (CI.isCiBuild || isFDroidBuild) publishingVersion else VersionUtils.getLatestSnapshotVersion("gradle-plugin")
