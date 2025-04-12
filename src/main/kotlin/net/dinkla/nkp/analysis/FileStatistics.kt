package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.Files
import java.io.File

fun fileStatistics(files: Files): List<FileStatistics> = files.map { FileStatistics.from(files, it) }

fun fileStatistics(
    files: Files,
    outputFile: File,
) {
    val stats = fileStatistics(files)
    logger.info { "Writing file statistics to ${outputFile.absolutePath}" }
    save(outputFile, stats)
}

@Serializable
data class FileStatistics(
    val file: String,
    val imports: Int,
    val classes: Int,
    val functions: Int,
    val properties: Int,
) {
    companion object {
        fun from(
            files: Files,
            file: AnalysedFile,
        ) = FileStatistics(
            file = files.relativePath(file.fileName.name),
            imports = file.imports.size,
            classes = file.classes.size,
            functions = file.functions.size,
            properties = file.properties.size,
        )
    }
}

private val logger = KotlinLogging.logger {}
