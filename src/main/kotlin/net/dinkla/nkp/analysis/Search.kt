package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.prettyPrint

fun Files.search(clazz: String,
) {
    logger.info { "*** searchClass ***" }
    val found = searchClassByName(clazz)
    found.forEach { println(it.prettyPrint()) }

    logger.info { "*** searchHierarchy ***" }
    val hier = searchHierarchy(clazz)
    hier.forEach { println(it.prettyPrint()) }

    logger.info { "*** searchImplementers ***" }
    val impls = searchImplementers(clazz)
    impls.forEach { println(it.prettyPrint()) }
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
