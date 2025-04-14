package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
class Project(
    val directory: String,
    val files: List<KotlinFile>,
) : List<KotlinFile> by files {
    fun packages(): List<Package> {
        val map = mutableMapOf<PackageName, MutableList<KotlinFile>>()
        for (file in files) {
            val packageName = file.packageName
            val list = map.getOrDefault(packageName, mutableListOf())
            list.add(file)
            map[packageName] = list
        }
        return map.map { Package(it.key, it.value) }.sortedBy { it.packageName.name }
    }

    fun relativePath(fileName: String): String = fileName.removePrefix(directory).removePrefix("/")
}
