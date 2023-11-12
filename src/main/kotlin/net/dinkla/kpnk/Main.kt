package net.dinkla.kpnk

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.elements.FileInfo
import net.dinkla.kpnk.elements.prettyPrint
import net.dinkla.kpnk.extract.extract
import net.dinkla.kpnk.extract.safeExtract
import java.io.File
import kotlin.system.exitProcess

private const val SCREEN_WIDTH = 120

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        exitProcess(-1)
    } else {
        println("Directory: $directory")
        println()
        val files = getAllKotlinFilesInDirectory(directory)
        val infos = fileInfos(files, directory, false)
        println("-".repeat(SCREEN_WIDTH))
        println("Reports")
        println("-".repeat(SCREEN_WIDTH))
        println(
            infos.groupBy { it.javaClass }.forEach {
                println("${it.key}: ${it.value.size}")
            },
        )
        println()
        val deps = dependencies(infos)
        val dependencies = Dependencies.from(deps)
        val string = Json.encodeToString(dependencies)
        File("dependencies.json").writeText(string)
    }
}

private fun fileInfos(
    files: List<String>,
    directory: String,
    safe: Boolean = true,
): List<FileInfo> {
    val results = mutableListOf<FileInfo>()
    for (fileName in files) {
        try {
            println("File: " + fileNameWithoutDirectory(directory, fileName))
            val tree = fromFile(fileName)
            val fileInfo = if (safe) safeExtract(tree) else extract(tree)
            results += FileInfo.Parsed(fileName, fileInfo)
            println(fileInfo.prettyPrint())
        } catch (e: Exception) {
            println("ERROR: " + e.message)
            results += FileInfo.Error(fileName, e.message!!)
        }
        println("-".repeat(SCREEN_WIDTH))
    }
    return results.toList()
}

internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
