package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.ClassSignature
import net.dinkla.nkp.domain.kotlinlang.Project

@Serializable
data class Search(
    val classes: List<ClassSignature>,
    val hierarchy: List<ClassSignature>,
    val implementers: List<ClassSignature>,
)

fun Project.search(className: String): Search {
    val clazz = getClass(className)
    val hier = getInheritanceHierarchy(className)
    val implementations = getImplementationsOf(className)
    return Search(clazz, hier, implementations)
}
