package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
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

    private val silent by option(help = "no output").flag(default = false)

    override fun run() {
        val files = readFiles(source)
        if (!silent) {
            reportErrors(files)
        }
        val project: Project = toProject(source, files)
        val json = Json.encodeToString(project)
        if (target != null) {
            target!!.writeText(json)
        } else {
            echo(json)
        }
    }
}

private fun readFiles(directory: File): List<Result<KotlinFile>> {
    val files = getAllKotlinFiles(directory)
    return runBlocking(Dispatchers.Default) {
        files
            .map {
                async {
                    extractFileInfo(it, directory.absolutePath)
                }
            }.map {
                it.await()
            }
    }
}

private fun toProject(
    directory: File,
    infos: List<Result<KotlinFile>>,
): Project = Project(directory.absolutePath, infos.filter { it.isSuccess }.map { it.getOrThrow() })

private fun extractFileInfo(
    fileName: String,
    prefix: String,
): Result<KotlinFile> {
    try {
        val withoutPrefix = fileName.removePrefix(prefix)
        val analysedFile = extract(FilePath(withoutPrefix), fromFile(fileName))
        return Result.success(analysedFile)
    } catch (e: Exception) {
        val message = "parsing '$fileName' yields ${e.message}"
        logger.error { message }
        return Result.failure(Error(message, e))
    }
}

private fun reportErrors(infos: List<Result<KotlinFile>>) {
    println(
        infos
            .groupBy { it.isSuccess }
            .map {
                "${it.value.size} ${if (it.key) "ok" else "exceptions"}"
            }.joinToString(", "),
    )
    val failures = infos.filter { it.isFailure }
    if (failures.isNotEmpty()) {
        println("ERROR: The following exceptions occurred:")
        var count = 1
        println("------------------------------------------------------------------------------")
        failures.forEach {
            val exception = it.exceptionOrNull()
            println("${count++}. ${exception?.message}")
            exception?.cause?.printStackTrace(System.out)
            println("------------------------------------------------------------------------------")
        }
    }
}

private val logger = KotlinLogging.logger {}
