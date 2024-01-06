package net.dinkla.kpnk.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import net.dinkla.kpnk.logger
import net.dinkla.kpnk.utilities.parseFilesFromDirectory

@Serializable
data class FileInfo(
    val fileName: FileName,
    val topLevel: TopLevel,
) {
    fun packageName(): String {
        val name = fileName.basename.replace(".kt", "")
        return topLevel.packageName.toString() + "." + name
    }
}

typealias FileInfos = List<FileInfo>

fun readFromDirectory(
    directory: String,
): FileInfos = runBlocking(Dispatchers.Default) {
    logger.info("Reading and saving from directory '$directory'")
    val allInfos = parseFilesFromDirectory(directory).map { it.await() }
    reportErrors(allInfos)
    allInfos.filter { it.isSuccess }.map { it.getOrThrow() }
}

private fun reportErrors(infos: List<Result<FileInfo>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info("${if (it.key) "Successful" else "With error"}: ${it.value.size}")
    }
}

