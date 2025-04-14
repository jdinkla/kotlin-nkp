package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.Project

fun fileStatistics(project: Project): FileStatistics = FileStatistics(project.map { FileStatistic.from(project, it) })

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
            project: Project,
            file: KotlinFile,
        ) = FileStatistic(
            file = project.relativePath(file.fileName.name),
            imports = file.imports.size,
            classes = file.classes.size,
            functions = file.functions.size,
            properties = file.properties.size,
        )
    }
}
