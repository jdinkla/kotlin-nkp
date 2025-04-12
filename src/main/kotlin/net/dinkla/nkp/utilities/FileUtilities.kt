package net.dinkla.nkp.utilities

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.FileName
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.extract.extract
import java.io.File

fun getAllKotlinFilesInDirectory(root: String): List<String> {
    val files = mutableListOf<String>()
    val file = File(root)
    if (file.isDirectory) {
        file.walk().forEach {
            addFileIfItMatches(it, files)
        }
    }
    return files.toList()
}

private fun addFileIfItMatches(
    it: File,
    files: MutableList<String>,
) {
    if (it.isFile && it.extension == "kt") {
        if (shouldFileBeAdded(it)) {
            files.add(it.absolutePath)
        }
    }
}

fun String.isTestDir() =
    this.contains("/test/") ||
        this.contains("/commonTest/") ||
        this.contains("/jvmTest/")

internal fun shouldFileBeAdded(it: File) =
    if (it.absolutePath.contains("/.idea/")) {
        logger.trace { "skipping file ${it.absolutePath}" }
        false
    } else if (it.absolutePath.isTestDir()) {
        logger.trace { "skipping test ${it.absolutePath}" }
        false
    } else {
        logger.trace { "adding file ${it.absolutePath}" }
        true
    }

internal fun CoroutineScope.parseFilesFromDirectory(directory: String): List<Deferred<Result<AnalysedFile>>> =
    fileInfos(getAllKotlinFilesInDirectory(directory), directory)

private fun CoroutineScope.fileInfos(
    files: List<String>,
    directory: String,
): List<Deferred<Result<AnalysedFile>>> =
    files.map {
        async {
            try {
                logger.trace { "handling file $it" }
                val withoutPrefix = it.removePrefix(directory)
                val file = extract(FileName(withoutPrefix), fromFile(it))
                Result.success(file)
            } catch (e: Exception) {
                logger.error { "parsing '$it' yields ${e.message}" }
                Result.failure(e)
            }
        }
    }

fun Files.saveToJsonFile(fileName: String) {
    logger.info { "Saving JSON to file '$fileName'" }
    val string = Json.encodeToString(this)
    File(fileName).writeText(string)
}

fun Files.Companion.read(file: File): Files =
    if (file.isDirectory) {
        Files.readFromDirectory(file.absolutePath)
    } else {
        Files.loadFromJsonFile(file.absolutePath)
    }

fun Files.Companion.loadFromJsonFile(fileName: String): Files {
    logger.info { "Reading from file '$fileName'" }
    val string = File(fileName).readText()
    return Json.decodeFromString<Files>(string)
}

fun Files.Companion.readFromDirectory(directory: String): Files =
    runBlocking(Dispatchers.Default) {
        logger.info { "Reading from directory '$directory'" }
        val allInfos = parseFilesFromDirectory(directory).map { it.await() }
        reportErrors(allInfos)
        Files(directory, allInfos.filter { it.isSuccess }.map { it.getOrThrow() })
    }

private fun reportErrors(infos: List<Result<AnalysedFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info { "${if (it.key) "Successful" else "With error"}: ${it.value.size}" }
    }
}

private val logger = KotlinLogging.logger {}
