package net.dinkla.kpnk

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

fun fileNameWithoutDirectory(directory: String, fileName: String): String {
    return fileName.substring(directory.length + 1)
}

fun basename(fileName: String): String {
    val index = max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\"))
    return if (index >= 0) {
        fileName.substring(index + 1)
    } else {
        fileName
    }
}
