package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.KotlinFile

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
            file: KotlinFile,
        ) = FileStatistic(
            file = files.relativePath(file.fileName.name),
            imports = file.imports.size,
            classes = file.classes.size,
            functions = file.functions.size,
            properties = file.properties.size,
        )
    }
}
