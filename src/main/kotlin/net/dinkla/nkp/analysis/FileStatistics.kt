package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.KotlinFile
import net.dinkla.nkp.domain.kotlinlang.Project

fun fileStatistics(project: Project): List<FileStatistics> = project.map { FileStatistics.from(project, it) }

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
            project: Project,
            file: KotlinFile,
        ) = FileStatistics(
            file = project.relativePath(file.filePath.path),
            imports = file.imports.size,
            classes = file.classes.size,
            functions = file.functions.size,
            properties = file.properties.size,
        )
    }
}
