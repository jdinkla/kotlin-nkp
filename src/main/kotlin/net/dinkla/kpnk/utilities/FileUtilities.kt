package net.dinkla.kpnk.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.extract.extract
import net.dinkla.kpnk.logger
import java.io.File
import kotlin.math.max

fun getAllKotlinFilesInDirectory(root: String): List<String> {
    val files = mutableListOf<String>()
    val file = File(root)
    if (file.isDirectory) {
        file.walk().forEach {
            if (it.isFile && it.extension == "kt") {
                files.add(it.absolutePath)
            }
        }
    }
    return files.toList()
}

@Serializable
@JvmInline
value class FileName(val name: String) {

    val basename: String
        get() {
            val index = max(name.lastIndexOf("/"), name.lastIndexOf("\\"))
            return if (index >= 0) {
                name.substring(index + 1)
            } else {
                name
            }
        }

    fun withoutDirectory(directory: String): String {
        return name.substring(directory.length + 1)
    }
}

internal fun load(fileName: String): List<FileInfo> {
    val string = File(fileName).readText()
    return Json.decodeFromString<List<FileInfo>>(string)
}

internal fun save(infos: List<FileInfo>, fileName: String) {
    val string = Json.encodeToString(infos)
    File(fileName).writeText(string)
}

internal fun CoroutineScope.parseFilesFromDirectory(directory: String): List<Deferred<Result<FileInfo>>> =
    fileInfos(getAllKotlinFilesInDirectory(directory))

private fun CoroutineScope.fileInfos(
    files: List<String>,
): List<Deferred<Result<FileInfo>>> = files.map {
    async {
        try {
            val tree = fromFile(it)
            val fileInfo = extract(tree)
            Result.success(FileInfo(FileName(it), fileInfo))
        } catch (e: Exception) {
            logger.error("parsing '$it' yields ${e.message}")
            Result.failure(e)
        }
    }
}

