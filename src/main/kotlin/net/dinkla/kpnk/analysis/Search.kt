package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.domain.prettyPrint
import net.dinkla.kpnk.logger

fun reportSearch(infos: FileInfos, clazz: String) {
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

fun FileInfos.searchClass(className: String): List<ClassSignature> =
    flatMap { fileInfo -> fileInfo.topLevel.classes }
        .filter { clazz -> clazz.name == className }

fun FileInfos.searchHierarchy(className: String): List<ClassSignature> {
    val cls = searchClass(className)
    return cls + cls.flatMap { clazz -> clazz.inheritedFrom }
        .flatMap { this.searchHierarchy(it) }
}

fun FileInfos.searchImplementers(className: String): List<ClassSignature> {
    return flatMap { fileInfo -> fileInfo.topLevel.classes }
        .filter { clazz -> clazz.inheritedFrom.contains(className) }
}
