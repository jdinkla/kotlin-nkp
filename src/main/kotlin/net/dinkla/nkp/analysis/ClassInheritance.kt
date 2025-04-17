package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project

@Serializable
data class ClassInheritance(
    val className: String,
    val packageName: PackageName,
    val numberOfSuperClasses: Int,
    val numberOfSubClasses: Int,
)

internal fun Project.inheritance(): List<ClassInheritance> =
    map { file ->
        file.classes.map {
            Pair(file.packageName, it)
        }
    }.flatten()
        .map {
            val name = it.second.name
            val h = getInheritanceHierarchy(name)
            val l = getImplementationsOf(name)
            ClassInheritance(name, it.first, h.size - 1, l.size)
        }.sortedByDescending { it.numberOfSuperClasses + it.numberOfSubClasses }
