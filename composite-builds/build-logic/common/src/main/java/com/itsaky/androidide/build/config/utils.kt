package com.itsaky.androidide.build.config

import java.io.File

private const val generatedWarning = "DO NOT EDIT - Automatically generated file"

fun File.replaceContents(
  dest: File,
  comment: String = "//",
  vararg candidates: Pair<String, String>
) {
  val contents = StringBuilder()
    .append(comment)
    .append(" ")
    .append(generatedWarning)
    .append(System.getProperty("line.separator").repeat(2))

  bufferedReader().use { reader ->
    reader.readText().also { text ->
      var t = text
      for ((old, new) in candidates) t = t.replace("@@${old}@@", new)
      contents.append(t)
    }
  }

  if (dest.exists()) dest.delete()
  dest.parentFile.let { if (!it.exists()) it.mkdirs() }
  dest.bufferedWriter().use { writer ->
    writer.write(contents.toString())
    writer.flush()
  }
}
