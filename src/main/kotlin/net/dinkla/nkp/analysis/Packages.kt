package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.Files
import java.io.File

fun packages(
    files: Files,
    outputFile: File
) {
    logger.info { "Writing package information to ${outputFile.absolutePath}" }
    save(outputFile, files.packages())
}

private val logger = KotlinLogging.logger {}
