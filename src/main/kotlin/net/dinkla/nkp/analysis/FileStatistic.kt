package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.Files

fun fileStatistics(files: Files): FileStatistics = FileStatistics(files.map { FileStatistic.from(files, it) })

@Serializable
data class FileStatistics(
    val fileStatistics: List<FileStatistic>,
)

@Serializable
data class FileStatistic(
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
        ) = FileStatistic(
            file = files.relativePath(file.fileName.name),
            imports = file.imports.size,
            classes = file.classes.size,
            functions = file.functions.size,
            properties = file.properties.size,
        )
    }
}
