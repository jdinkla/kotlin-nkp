package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
class Project(
    val directory: String,
    val files: List<KotlinFile>,
) : List<KotlinFile> by files {
    fun packages(): List<Package> =
        files
            .groupBy { it.packageName }
            .map { (name, files) -> Package(name, files) }
            .sortedBy { it.packageName.name }

    fun relativePath(fileName: String): String = fileName.removePrefix(directory).removePrefix("/")

    fun getClass(className: String): List<ClassSignature> =
        flatMap { it.classes }
            .filter { it.name == className }

    fun getSuperClasses(className: String): List<ClassSignature> {
        val clazz = getClass(className)
        return clazz +
            clazz
                .flatMap { it.superTypes }
                .flatMap { getSuperClasses(it) }
    }

    fun getSubClasses(className: String): List<ClassSignature> =
        flatMap { it.classes }
            .filter { it.superTypes.contains(className) }
}
