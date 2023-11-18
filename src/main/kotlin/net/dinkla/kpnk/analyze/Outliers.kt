package net.dinkla.kpnk.analyze

import net.dinkla.kpnk.elements.ClassSignature
import net.dinkla.kpnk.elements.FileInfo

fun reportLargeClasses(infos: List<FileInfo>, topN: Int = 10) {
    largeClasses(infos, topN).forEach { c ->
        println("${c.name} has ${c.functions.size} functions and ${c.properties.size} properties")
    }
}

fun largeClasses(infos: List<FileInfo>, topN: Int): List<ClassSignature> {
    val allClasses = infos.flatMap { it.topLevel.classes }
    return allClasses.sortedByDescending { it.functions.size + it.properties.size }.take(topN)
}
