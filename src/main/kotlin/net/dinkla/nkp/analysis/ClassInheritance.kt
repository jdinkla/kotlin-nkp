package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project

@Serializable
data class ClassInheritance(
    val className: String,
    val packageName: PackageName,
    val superClassCount: Int,
    val subClassCount: Int,
)

internal fun Project.inheritance(): List<ClassInheritance> =
    map { file ->
        file.classes.map {
            Pair(file.packageName, it)
        }
    }.flatten()
        .map {
            val name = it.second.name
            val superClassCount = getSuperClasses(name).size - 1
            val subClassCount = getSubClasses(name).size
            ClassInheritance(name, it.first, superClassCount, subClassCount)
        }.sortedByDescending { it.superClassCount + it.subClassCount }
