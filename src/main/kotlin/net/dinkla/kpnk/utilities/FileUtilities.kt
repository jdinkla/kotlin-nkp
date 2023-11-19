package net.dinkla.kpnk.utilities

import kotlinx.serialization.Serializable
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
