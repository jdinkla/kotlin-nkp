package net.dinkla.nkp.utilities

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.FileName
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.extract.extract
import java.io.File

internal fun getAllKotlinFiles(directory: File): List<String> =
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
        val files = getAllKotlinFiles(directory)
        val infos = fileInfos(files, directory.absolutePath) //.map { it.await() }
        reportErrors(infos)
        Files(directory.absolutePath, infos.filter { it.isSuccess }.map { it.getOrThrow() })
    }

private fun fileInfos(files: List<String>, directory: String): List<Result<AnalysedFile>> =
    files.map { extractFileInfo(it, directory) }

private fun extractFileInfo(fileName: String, directory: String): Result<AnalysedFile> {
    try {
        logger.trace { "handling file $fileName" }
        val withoutPrefix = fileName.removePrefix(directory)
        val analysedFile = extract(FileName(withoutPrefix), fromFile(fileName))
        return Result.success(analysedFile)
    } catch (e: Exception) {
        logger.error { "parsing '$fileName' yields ${e.message}" }
        return Result.failure(e)
    }
}

private val logger = KotlinLogging.logger {}
