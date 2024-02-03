package net.dinkla.kpnk.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.logger
import net.dinkla.kpnk.utilities.parseFilesFromDirectory
import java.io.File

@Serializable
data class FileInfo(
    val topLevel: TopLevel,
) {
    companion object {
        fun readFromDirectory(directory: String): FileInfos =
            runBlocking(Dispatchers.Default) {
                logger.info("Reading and saving from directory '$directory'")
                val allInfos = parseFilesFromDirectory(directory).map { it.await() }
                reportErrors(allInfos)
                allInfos.filter { it.isSuccess }.map { it.getOrThrow() }
            }

        fun loadFromJsonFile(fileName: String): FileInfos {
            val string = File(fileName).readText()
            return Json.decodeFromString<List<FileInfo>>(string)
        }

        fun saveToJsonFile(
            infos: FileInfos,
            fileName: String,
        ) {
            val string = Json.encodeToString(infos)
            File(fileName).writeText(string)
        }
    }
}

typealias FileInfos = List<FileInfo>

private fun reportErrors(infos: List<Result<FileInfo>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}
