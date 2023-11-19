package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

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
