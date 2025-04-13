package net.dinkla.nkp.utilities

import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.Files
import java.io.File

internal fun getAllKotlinFiles(directory: File): List<String> = if (directory.isDirectory) {
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

inline fun <reified T> File.saveJson(entity: T) {
    writeText(Json.encodeToString<T>(entity))
}

fun Files.Companion.loadFromJsonFile(fileName: String): Files {
    val string = File(fileName).readText()
    return Json.decodeFromString<Files>(string)
}
