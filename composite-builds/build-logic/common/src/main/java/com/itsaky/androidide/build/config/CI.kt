package com.itsaky.androidide.build.config

import java.io.File

object CI {
  val commitHash by lazy {
    check(isGitRepo) { "This build is not a Git repository." }
    val sha = System.getenv("GITHUB_SHA") ?: "HEAD"
    cmdOutput("git", "rev-parse", "--short", sha)
  }

  val branchName by lazy {
    check(isGitRepo) { "This build is not a Git repository." }
    System.getenv("GITHUB_REF_NAME") ?: cmdOutput("git", "rev-parse", "--abbrev-ref", "HEAD")
  }

  val isGitRepo by lazy {
    cmdOutput("git", "rev-parse", "--is-inside-work-tree").trim() == "true"
  }

  val isCiBuild by lazy { "true" == System.getenv("CI") }
  val isTestEnv by lazy { "true" == System.getenv("ANDROIDIDE_TEST") }

  private fun cmdOutput(vararg args: String): String {
    return ProcessBuilder(*args)
      .directory(File("."))
      .redirectErrorStream(true)
      .start()
      .inputStream
      .bufferedReader()
      .readText()
      .trim()
  }
}
