package net.dinkla.nkp.utilities

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.FileName
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.extract.extract
import java.io.File

fun getAllKotlinFilesInDirectory(directory: File): List<String> =
    if (directory.isDirectory) {
        directory
            .walk()
            .filter { it.isFile && isRelevant(it) }
            .map { it.absolutePath }
            .toList()
    } else {
        listOf()
    }

internal fun isRelevant(file: File) =
    file.extension == "kt" && testDirIdentifiers.none { file.absolutePath.contains(it) }

private val testDirIdentifiers = listOf("/.idea/", "/test/", "/commonTest/", "/jvmTest/")

internal fun CoroutineScope.parseFilesFromDirectory(directory: File): List<Deferred<Result<AnalysedFile>>> =
    fileInfos(getAllKotlinFilesInDirectory(directory), directory.absolutePath)

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

fun Files.Companion.loadFromJsonFile(fileName: String): Files {
    logger.info { "Reading from file '$fileName'" }
    val string = File(fileName).readText()
    return Json.decodeFromString<Files>(string)
}

fun Files.Companion.readFromDirectory(directory: File): Files =
    runBlocking(Dispatchers.Default) {
        logger.info { "Reading from directory '$directory'" }
        val allInfos = parseFilesFromDirectory(directory).map { it.await() }
        reportErrors(allInfos)
        Files(directory.absolutePath, allInfos.filter { it.isSuccess }.map { it.getOrThrow() })
    }

private fun reportErrors(infos: List<Result<AnalysedFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info { "${if (it.key) "Successful" else "With error"}: ${it.value.size}" }
    }
}

private val logger = KotlinLogging.logger {}
