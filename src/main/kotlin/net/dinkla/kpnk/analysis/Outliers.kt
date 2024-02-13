package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.kpnk.domain.AnalysedFile
import net.dinkla.kpnk.domain.ClassSignature

fun outliers(
    infos: List<AnalysedFile>,
    topN: Int = 10,
) {
    logger.info { "*** Large Classes ***" }
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

private val logger = KotlinLogging.logger {}
