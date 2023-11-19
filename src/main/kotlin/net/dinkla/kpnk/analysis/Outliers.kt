package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.logger

fun reportLargeClasses(infos: List<FileInfo>, topN: Int = 10) {
    logger.info("*** Large Classes ***")
    largeClasses(infos, topN).forEach { c ->
        println(
            """
            ${c.name} has ${c.functions.size} functions and ${c.properties.size} properties (total ${c.declarations.size})
            """.trimIndent(),
        )
    }
}

fun largeClasses(infos: List<FileInfo>, topN: Int): List<ClassSignature> {
    val allClasses = infos.flatMap { it.topLevel.classes }
    return allClasses.sortedByDescending { it.declarations.size }.take(topN)
}
