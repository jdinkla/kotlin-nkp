package net.dinkla.kpnk

import java.io.File

// read all kotlin files in the given directory and its subdirectories
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
