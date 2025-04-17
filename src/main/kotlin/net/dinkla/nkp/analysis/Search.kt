package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.ClassSignature
import net.dinkla.nkp.domain.kotlinlang.Project

@Serializable
data class Search(
    val classes: List<ClassSignature>,
    val superClasses: List<ClassSignature>,
    val subClasses: List<ClassSignature>,
)

fun Project.search(className: String): Search {
    val classes = getClass(className)
    val superClasses = getSuperClasses(className)
    val subClasses = getSubClasses(className)
    return Search(classes, superClasses, subClasses)
}
