package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.file
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.extract.extract
import net.dinkla.nkp.utilities.fromFile
import net.dinkla.nkp.utilities.getAllKotlinFiles
import java.io.File

class Parse : CliktCommand(name = "parse") {
    override fun help(context: Context) = "Parse a source directory and generate a model file."

    private val source by argument(
        help = "Path to the source directory to analyze",
    ).file(mustExist = true, canBeDir = true, canBeFile = false)

    private val target by argument(
        help = "The output file",
    ).convert { File(it) }
        .optional()

    override fun run() {
        val project: Project = readFromDirectory(source)
        val json = Json.encodeToString(project)
        if (target != null) {
            target!!.writeText(json)
        } else {
            echo(json)
        }
    }
}

internal fun readFromDirectory(directory: File): Project {
    val files = getAllKotlinFiles(directory)
    val infos =
        runBlocking(Dispatchers.Default) {
            files
                .map {
                    async {
                        extractFileInfo(it, directory.absolutePath)
                    }
                }.map {
                    it.await()
                }
        }
    reportErrors(infos)
    return Project(directory.absolutePath, infos.filter { it.isSuccess }.map { it.getOrThrow() })
}

private fun extractFileInfo(
    fileName: String,
    prefix: String,
): Result<KotlinFile> {
    try {
        val withoutPrefix = fileName.removePrefix(prefix)
        val analysedFile = extract(FilePath(withoutPrefix), fromFile(fileName))
        return Result.success(analysedFile)
    } catch (e: Exception) {
        logger.error { "parsing '$fileName' yields ${e.message}" }
        return Result.failure(e)
    }
}

internal fun reportErrors(infos: List<Result<KotlinFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info { "${if (it.key) "Successful" else "With error"}: ${it.value.size}" }
    }
}

private val logger = KotlinLogging.logger {}
