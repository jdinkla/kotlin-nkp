package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.file
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.kotlinlang.KotlinFile
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.extract.extract
import net.dinkla.nkp.utilities.fromFile
import net.dinkla.nkp.utilities.getAllKotlinFiles
import java.io.File

class Parse : CliktCommand(name = "parse") {
    override fun help(context: Context) =
        "Parse source directories and generate a model file. This is the necessary " +
            "first step before running any analysis. Multiple source directories can be " +
            "specified using --sources option."

    private val source by argument(
        help = "Path to the primary source directory to analyze",
    ).file(mustExist = true, canBeDir = true, canBeFile = false)

    private val additionalSources by option(
        "--sources",
        "-s",
        help = "Additional source directories (comma-separated)",
    ).file(mustExist = true, canBeDir = true, canBeFile = false).split(",")

    private val target by argument(
        help = "The output file",
    ).convert { File(it) }
        .optional()

    private val silent by option(help = "no output").flag(default = false)

    override fun run() {
        val allSources = listOf(source) + (additionalSources ?: emptyList())
        val allFiles = allSources.flatMap { readFiles(it) }
        if (!silent) {
            reportErrors(allFiles)
        }
        val project: Project = toProject(allSources, allFiles)
        val json = Json.encodeToString(project)
        if (target != null) {
            target!!.writeText(json)
        } else {
            echo(json)
        }
    }

    private fun reportErrors(infos: List<Result<KotlinFile>>) {
        echo(
            infos
                .groupBy { it.isSuccess }
                .map {
                    "${it.value.size} ${if (it.key) "ok" else "exceptions"}"
                }.joinToString(", "),
        )
        val failures = infos.filter { it.isFailure }
        if (failures.isNotEmpty()) {
            echo("ERROR: The following exceptions occurred:")
            var count = 1
            echo("------------------------------------------------------------------------------")
            failures.forEach {
                val exception = it.exceptionOrNull()
                echo("${count++}. ${exception?.message}")
                exception?.cause?.printStackTrace(System.out)
                echo("------------------------------------------------------------------------------")
            }
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
    directories: List<File>,
    infos: List<Result<KotlinFile>>,
): Project {
    val absolutePaths = directories.map { it.absolutePath }
    return Project(
        directory = absolutePaths.first(),
        files = infos.filter { it.isSuccess }.map { it.getOrThrow() },
        directories = absolutePaths,
    )
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
        val message = "parsing '$fileName' yields ${e.message}"
        logger.error { message }
        return Result.failure(Error(message, e))
    }
}

private val logger = KotlinLogging.logger {}
