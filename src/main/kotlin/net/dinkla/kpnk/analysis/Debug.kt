package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.kpnk.domain.Files

fun debug(files: Files) {
    logger.info { "*************** DEBUG ***************" }
    val ds = AnalyzedPackage.from(files)
    println(ds)
}

private val logger = KotlinLogging.logger {}
