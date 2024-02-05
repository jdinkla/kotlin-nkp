package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.prettyPrint
import net.dinkla.kpnk.logger

fun search(
    infos: Files,
    clazz: String,
) {
    logger.info("*** searchClass ***")
    val found = infos.searchClass(clazz)
    found.forEach { println(it.prettyPrint()) }

    logger.info("*** searchHierarchy ***")
    val hier = infos.searchHierarchy(clazz)
    hier.forEach { println(it.prettyPrint()) }

    logger.info("*** searchImplementers ***")
    val impls = infos.searchImplementers(clazz)
    impls.forEach { println(it.prettyPrint()) }
}

fun Files.searchClass(className: String): List<ClassSignature> =
    flatMap { file -> file.classes }
        .filter { clazz -> clazz.name == className }

fun Files.searchHierarchy(className: String): List<ClassSignature> {
    val cls = searchClass(className)
    return cls +
        cls.flatMap { clazz -> clazz.inheritedFrom }
            .flatMap { this.searchHierarchy(it) }
}

fun Files.searchImplementers(className: String): List<ClassSignature> {
    return flatMap { file -> file.classes }
        .filter { clazz -> clazz.inheritedFrom.contains(className) }
}
