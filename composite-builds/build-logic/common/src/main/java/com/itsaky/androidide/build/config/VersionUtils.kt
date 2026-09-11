package com.itsaky.androidide.build.config

import org.gradle.api.GradleException
import java.io.BufferedInputStream
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

object VersionUtils {
  const val SONATYPE_SNAPSHOTS_REPO = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
  const val SONATYPE_PUBLIC_REPO = "https://s01.oss.sonatype.org/content/groups/public/"
  const val LATEST_INTEGRATION = "latest.integration"
  private var cachedVersion: String? = null

  @JvmStatic
  fun getLatestSnapshotVersion(artifact: String): String {
    cachedVersion?.also { cached ->
      println("Found latest version of artifact '$artifact' : '$cached' (cached)")
      return cached
    }

    val groupId = BuildConfig.packageName.replace('.', '/')
    val moduleMetadata = "$SONATYPE_SNAPSHOTS_REPO/$groupId/$artifact/maven-metadata.xml"
    return try {
      BufferedInputStream(URI.create(moduleMetadata).toURL().openStream()).use { inputStream ->
        val builderFactory = DocumentBuilderFactory.newInstance()
        val builder = builderFactory.newDocumentBuilder()
        val document = builder.parse(inputStream)
        val xPathFactory = XPathFactory.newInstance()
        val xPath = xPathFactory.newXPath()
        val latestVersion = xPath.evaluate("/metadata/versioning/latest", document)
        cachedVersion = latestVersion
        println("Found latest version of artifact '$artifact' : '$latestVersion'")
        latestVersion
      }
    } catch (err: Throwable) {
      if (CI.isCiBuild) throw GradleException("Failed to download: $moduleMetadata", err)
      println("Failed to download $moduleMetadata: ${err.message}")
      LATEST_INTEGRATION
    }
  }
}
