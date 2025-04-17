package net.dinkla.nkp.domain.kotlinlang

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

    fun getClass(className: String): List<ClassSignature> =
        flatMap { it.classes }
            .filter { it.name == className }

    fun getInheritanceHierarchy(className: String): List<ClassSignature> {
        val clazz = getClass(className)
        return clazz +
            clazz
                .flatMap { it.superTypes }
                .flatMap { getInheritanceHierarchy(it) }
    }

    fun getImplementationsOf(className: String): List<ClassSignature> =
        flatMap { it.classes }
            .filter { it.superTypes.contains(className) }
}
