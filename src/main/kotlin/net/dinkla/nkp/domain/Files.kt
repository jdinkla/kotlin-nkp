package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
class Files(val directory: String, private val files: List<AnalysedFile>) : List<AnalysedFile> by files {
    fun packages(): List<Package> {
        val map = mutableMapOf<PackageName, MutableList<AnalysedFile>>()
        for (file in files) {
            val packageName = file.packageName
            val list = map.getOrDefault(packageName, mutableListOf())
            list.add(file)
            map[packageName] = list
        }
        return map.map { Package(it.key, it.value) }.sortedBy { it.packageName.name }
    }

    fun relativePath(fileName: String): String {
        return fileName.removePrefix(directory).removePrefix("/")
    }
}
