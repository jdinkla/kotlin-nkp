package net.dinkla.kpnk.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.logger
import net.dinkla.kpnk.utilities.parseFilesFromDirectory
import java.io.File

object FileInfo {
    fun readFromDirectory(directory: String): Files =
        runBlocking(Dispatchers.Default) {
            logger.info("Reading and saving from directory '$directory'")
            val allInfos = parseFilesFromDirectory(directory).map { it.await() }
            reportErrors(allInfos)
            allInfos.filter { it.isSuccess }.map { it.getOrThrow() }
        }

    fun loadFromJsonFile(fileName: String): Files {
        val string = File(fileName).readText()
        return Json.decodeFromString<List<AnalysedFile>>(string)
    }

    fun saveToJsonFile(
        infos: Files,
        fileName: String,
    ) {
        val string = Json.encodeToString(infos)
        File(fileName).writeText(string)
    }
}

private fun reportErrors(infos: List<Result<AnalysedFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}
