package net.dinkla.kpnk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.analyze.Dependencies
import net.dinkla.kpnk.analyze.dependencies
import net.dinkla.kpnk.analyze.reportLargeClasses
import net.dinkla.kpnk.elements.FileInfo
import net.dinkla.kpnk.extract.extract
import net.dinkla.kpnk.extract.safeExtract
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
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
        runBlocking(Dispatchers.Default) {
            when (command) {
                Command.ANALYZE -> {
                    logger.info("Directory: $directory")
                    val allInfos = parseFilesFromDirectory(directory).map { it.await() }
                    reportErrors(allInfos)
                    infos = allInfos.filter { it.isSuccess }.map { it.getOrThrow() }
                    save(infos, "infos.json")
                }

                Command.LOAD -> {
                    infos = load("infos.json")
                }
            }
        }
        reportDependencies(infos)
        reportLargeClasses(infos)
    }
}

private fun load(fileName: String): List<FileInfo> {
    val string = File(fileName).readText()
    return Json.decodeFromString<List<FileInfo>>(string)
}

private fun save(infos: List<FileInfo>, fileName: String) {
    val string = Json.encodeToString(infos)
    File(fileName).writeText(string)
}

private fun reportDependencies(infos: List<FileInfo>) {
    val dependencies = Dependencies.from(dependencies(infos))
    val string = Json.encodeToString(dependencies)
    File("dependencies.json").writeText(string)
}

private fun reportErrors(infos: List<Result<FileInfo>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}

private fun CoroutineScope.parseFilesFromDirectory(directory: String): List<Deferred<Result<FileInfo>>> =
    fileInfos(getAllKotlinFilesInDirectory(directory), false)

private fun CoroutineScope.fileInfos(
    files: List<String>,
    safe: Boolean = true,
): List<Deferred<Result<FileInfo>>> = files.map {
    async {
        try {
            val tree = fromFile(it)
            val fileInfo = if (safe) safeExtract(tree) else extract(tree)
            success(FileInfo(FileName(it), fileInfo))
        } catch (e: Exception) {
            logger.error("parsing '$it' yields ${e.message}")
            failure(e)
        }
    }
}

internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
