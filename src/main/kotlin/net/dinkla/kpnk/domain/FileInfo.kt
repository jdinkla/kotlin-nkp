package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.utilities.FileName

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
