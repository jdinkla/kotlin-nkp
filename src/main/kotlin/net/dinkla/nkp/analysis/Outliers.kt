package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.ClassSignature

fun outliers(files: List<AnalysedFile>): List<Sizes> {
    return largeClasses(files).map {
        Sizes(it.name, it.classes.size, it.functions.size, it.properties.size, it.declarations.size)
    }
}

@Serializable
data class Sizes(
    val className: String,
    val classes: Int,
    val functions: Int,
    val properties: Int,
    val declarations: Int,
)

internal fun largeClasses(files: List<AnalysedFile>): List<ClassSignature> =
    files.flatMap { it.classes }.sortedByDescending { it.declarations.size }
