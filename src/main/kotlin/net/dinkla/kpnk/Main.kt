package net.dinkla.kpnk

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.analyze.Dependencies
import net.dinkla.kpnk.analyze.dependencies
import net.dinkla.kpnk.elements.FileInfo
import net.dinkla.kpnk.elements.prettyPrint
import net.dinkla.kpnk.extract.extract
import net.dinkla.kpnk.extract.safeExtract
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

private val logger = LoggerFactory.getLogger("Main")

enum class Command {
    ANALYZE, LOAD
}

val command = Command.ANALYZE

fun main(args: Array<String>) {
    val directoryString = parseArgs(args)
    if (directoryString == null) {
        exitProcess(-1)
    } else {
        val directory = File(directoryString).absolutePath
        val infos: List<FileInfo>
        when (command) {
            Command.ANALYZE -> {
                infos = parseFilesFromDirectory(directory)
                save(infos, "infos.json")
            }

            Command.LOAD -> {
                infos = load("infos.json")
            }
        }
        reportErrors(infos)
        reportDependencies(infos)
        for (i in infos.filterIsInstance<FileInfo.Parsed>()) {
            for (c in i.elements.classes) {
                println("${c.name} ${c.functions.size} ${c.parameters.size}")
            }
        }
    }
}

private fun load(fileName: String): List<FileInfo> {
    val string = File(fileName).readText()
    val infos = Json.decodeFromString<List<FileInfo.Parsed>>(string)
    return infos
}

private fun save(infos: List<FileInfo>, fileName: String) {
    val parsed = infos.filterIsInstance<FileInfo.Parsed>()
    val string = Json.encodeToString(parsed)
    File(fileName).writeText(string)
}

private fun reportDependencies(infos: List<FileInfo>) {
    val dependencies = Dependencies.from(dependencies(infos))
    val string = Json.encodeToString(dependencies)
    File("dependencies.json").writeText(string)
}

private fun reportErrors(infos: List<FileInfo>) {
    infos.groupBy { it.javaClass }.forEach {
        logger.info("${it.key}: ${it.value.size}")
    }
}

private fun parseFilesFromDirectory(directory: String): List<FileInfo> {
    logger.info("Directory: $directory")
    val files = getAllKotlinFilesInDirectory(directory)
    val infos = fileInfos(files, directory, false)
    return infos
}

private fun fileInfos(
    files: List<String>,
    directory: String,
    safe: Boolean = true,
): List<FileInfo> {
    val results = mutableListOf<FileInfo>()
    for (fileName in files) {
        try {
            logger.info("File: " + fileNameWithoutDirectory(directory, fileName))
            val tree = fromFile(fileName)
            val fileInfo = if (safe) safeExtract(tree) else extract(tree)
            results += FileInfo.Parsed(fileName, fileInfo)
            logger.info(fileInfo.prettyPrint())
        } catch (e: Exception) {
            logger.error("ERROR: " + e.message)
            results += FileInfo.Error(fileName, e.message ?: "Unknown error")
        }
    }
    return results.toList()
}

internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
