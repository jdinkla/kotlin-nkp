package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.Files

fun debug(files: Files) {
    logger.info { "*************** DEBUG ***************" }
    val ds = AnalyzedPackage.from(files)
    println(ds)
}

private val logger = KotlinLogging.logger {}
