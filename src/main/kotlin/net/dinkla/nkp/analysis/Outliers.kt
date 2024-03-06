package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.ClassSignature

fun outliers(
    files: List<AnalysedFile>,
    topN: Int = 10,
) {
    logger.info { "*** Large Classes ***" }
    largeClasses(files, topN).forEach { c ->
        println(
            """
            ${c.name} has ${c.classes.size} classes, ${c.functions.size} functions and ${c.properties.size} properties (total ${c.declarations.size})
            """.trimIndent(),
        )
    }
}

internal fun largeClasses(
    files: List<AnalysedFile>,
    topN: Int,
): List<ClassSignature> {
    val allClasses = files.flatMap { it.classes }
    return allClasses.sortedByDescending { it.declarations.size }.take(topN)
}

private val logger = KotlinLogging.logger {}
