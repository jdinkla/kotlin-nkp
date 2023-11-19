package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfos

fun FileInfos.searchClass(className: String): List<ClassSignature> =
    flatMap { fileInfo -> fileInfo.topLevel.classes }
        .filter { clazz -> clazz.name == className }

fun FileInfos.searchHierarchy(className: String): List<ClassSignature> {
    val cls = searchClass(className)
    return cls + cls.flatMap { clazz -> clazz.inheritedFrom }
        .flatMap { this.searchHierarchy(it) }
}
