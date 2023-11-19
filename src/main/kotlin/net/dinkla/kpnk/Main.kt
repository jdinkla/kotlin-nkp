package net.dinkla.kpnk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.dinkla.kpnk.analysis.reportDependencies
import net.dinkla.kpnk.analysis.reportLargeClasses
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.utilities.load
import net.dinkla.kpnk.utilities.parseFilesFromDirectory
import net.dinkla.kpnk.utilities.save
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

val logger: Logger = LoggerFactory.getLogger("Main")

enum class Command {
    READ_AND_SAVE, LOAD
}

val command = Command.READ_AND_SAVE

// val command = Command.LOAD
const val SAVE_FILE_NAME = "infos.json"

fun main(args: Array<String>) {
    val directoryString = parseArgs(args)
    if (directoryString == null) {
        exitProcess(-1)
    } else {
        val directory = File(directoryString).absolutePath
        val infos = when (command) {
            Command.READ_AND_SAVE -> {
                read(directory, SAVE_FILE_NAME)
            }

            Command.LOAD -> {
                load(SAVE_FILE_NAME)
            }
        }
        reportDependencies(infos)
        reportLargeClasses(infos)
    }
}

private fun read(
    directory: String,
    saveFileName: String,
): List<FileInfo> = runBlocking(Dispatchers.Default) {
    logger.info("Reading and saving from directory '$directory'")
    val allInfos = parseFilesFromDirectory(directory).map { it.await() }
    reportErrors(allInfos)
    val filtered = allInfos.filter { it.isSuccess }.map { it.getOrThrow() }
    save(filtered, saveFileName)
    logger.info("saved to file '$saveFileName'")
    filtered
}

private fun reportErrors(infos: List<Result<FileInfo>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}

internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
