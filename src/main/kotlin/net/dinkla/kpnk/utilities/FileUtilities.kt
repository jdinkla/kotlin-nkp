package net.dinkla.kpnk.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.FileName
import net.dinkla.kpnk.extract.extract
import net.dinkla.kpnk.logger
import java.io.File

fun getAllKotlinFilesInDirectory(root: String): List<String> {
    val files = mutableListOf<String>()
    val file = File(root)
    if (file.isDirectory) {
        file.walk().forEach {
            if (it.isFile && it.extension == "kt") {
                if (it.absolutePath.contains("/.idea/")) {
                    logger.trace("skipping file ${it.absolutePath}")
                } else if (it.absolutePath.contains("/test/") && it.name.endsWith("Test.kt")) {
                    logger.trace("skipping test  ${it.absolutePath}")
                } else {
                    files.add(it.absolutePath)
                    logger.trace("adding file ${it.absolutePath}")
                }
            }
        }
    }
    return files.toList()
}

internal fun CoroutineScope.parseFilesFromDirectory(directory: String): List<Deferred<Result<FileInfo>>> =
    fileInfos(getAllKotlinFilesInDirectory(directory))

private fun CoroutineScope.fileInfos(
    files: List<String>,
): List<Deferred<Result<FileInfo>>> = files.map {
    async {
        try {
            logger.trace("handling file $it")
            val tree = fromFile(it)
            val fileInfo = extract(tree)
            Result.success(FileInfo(FileName(it), fileInfo))
        } catch (e: Exception) {
            logger.error("parsing '$it' yields ${e.message}")
            Result.failure(e)
        }
    }
}
