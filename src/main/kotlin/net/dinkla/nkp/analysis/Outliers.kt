package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.KotlinFile

fun outliers(files: List<KotlinFile>): List<Sizes> =
    largeClasses(files).map {
        Sizes(it.name, it.classes.size, it.functions.size, it.properties.size, it.declarations.size)
    }

@Serializable
data class Sizes(
    val className: String,
    val classes: Int,
    val functions: Int,
    val properties: Int,
    val declarations: Int,
)

internal fun largeClasses(files: List<KotlinFile>): List<ClassSignature> =
    files.flatMap { it.classes }.sortedByDescending { it.declarations.size }
