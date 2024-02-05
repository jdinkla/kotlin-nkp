package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.AnalysedFile
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.logger

object Outliers {
    fun execute(
        files: Files,
    ) {
        reportLargeClasses(files)
    }
}

internal fun reportLargeClasses(
    infos: List<AnalysedFile>,
    topN: Int = 10,
) {
    logger.info("*** Large Classes ***")
    largeClasses(infos, topN).forEach { c ->
        println(
            """
            ${c.name} has ${c.classes.size} classes, ${c.functions.size} functions and ${c.properties.size} properties (total ${c.declarations.size})
            """.trimIndent(),
        )
    }
}

internal fun largeClasses(
    infos: List<AnalysedFile>,
    topN: Int,
): List<ClassSignature> {
    val allClasses = infos.flatMap { it.classes }
    return allClasses.sortedByDescending { it.declarations.size }.take(topN)
}
