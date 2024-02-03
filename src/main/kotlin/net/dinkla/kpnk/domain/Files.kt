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
class Files(private val items: List<AnalysedFile>) : List<AnalysedFile> by items {
    fun packages(): List<Package> {
        val map = mutableMapOf<PackageName, MutableList<AnalysedFile>>()
        for (file in items) {
            val packageName = file.packageName
            val list = map.getOrDefault(packageName, mutableListOf())
            list.add(file)
            map[packageName] = list
        }
        return map.map { Package(it.key, it.value) }
    }

    fun saveToJsonFile(fileName: String) {
        val string = Json.encodeToString(this)
        File(fileName).writeText(string)
    }

    companion object {
        fun loadFromJsonFile(fileName: String): Files {
            val string = File(fileName).readText()
            return Json.decodeFromString<Files>(string)
        }

        fun readFromDirectory(directory: String): Files =
            runBlocking(Dispatchers.Default) {
                logger.info("Reading and saving from directory '$directory'")
                val allInfos = parseFilesFromDirectory(directory).map { it.await() }
                reportErrors(allInfos)
                Files(allInfos.filter { it.isSuccess }.map { it.getOrThrow() })
            }
    }
}

private fun reportErrors(infos: List<Result<AnalysedFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}
