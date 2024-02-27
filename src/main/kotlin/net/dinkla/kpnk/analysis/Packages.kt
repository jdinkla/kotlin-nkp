package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.kpnk.domain.Files
import java.io.File

fun packages(
    files: Files,
    outputFile: File
) {
    logger.info { "Writing package information to ${outputFile.absolutePath}" }
    save(outputFile, files.packages())
}

private val logger = KotlinLogging.logger {}
