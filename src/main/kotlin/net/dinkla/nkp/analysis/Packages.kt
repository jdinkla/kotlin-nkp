package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.Project
import java.io.File

fun packages(
    project: Project,
    outputFile: File,
) {
    logger.info { "Writing package information to ${outputFile.absolutePath}" }
    save(outputFile, project.packages())
}

private val logger = KotlinLogging.logger {}
