package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.Files

@Serializable
data class Search(
    val classes: List<ClassSignature>,
    val hierarchy: List<ClassSignature>,
    val implementers: List<ClassSignature>,
)

fun Files.search(clazz: String): Search {
    val found = searchClassByName(clazz)
    val hier = searchHierarchy(clazz)
    val impls = searchImplementers(clazz)
    return Search(found, hier, impls)
}

fun Files.searchClassByName(className: String): List<ClassSignature> =
    flatMap { file -> file.classes }
        .filter { clazz -> clazz.name == className }

fun Files.searchHierarchy(className: String): List<ClassSignature> {
    val cls = searchClassByName(className)
    return cls +
        cls
            .flatMap { clazz -> clazz.inheritedFrom }
            .flatMap { this.searchHierarchy(it) }
}

fun Files.searchImplementers(className: String): List<ClassSignature> =
    flatMap { file -> file.classes }
        .filter { clazz -> clazz.inheritedFrom.contains(className) }

private val logger = KotlinLogging.logger {}
