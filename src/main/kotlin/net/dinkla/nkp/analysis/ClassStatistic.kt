package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.ClassModifier
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.InheritanceModifier
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.VisibilityModifier

fun classStatistics(files: Files): ClassStatistics =
    ClassStatistics(
        files
            .flatMap { file ->
                file.classes.map {
                    ClassStatistic.from(file.packageName, it)
                }
            }.sortedBy { "${it.packageName}-${it.className}" },
    )

@Serializable
data class ClassStatistics(
    val classStatistics: List<ClassStatistic>,
)

@Serializable
data class ClassStatistic(
    val className: String,
    val packageName: PackageName,
    val parameters: Int = 0,
    val inheritedFrom: Int = 0,
    val visibilityModifier: VisibilityModifier? = null,
    val elementType: ClassSignature.Type = ClassSignature.Type.CLASS,
    val classModifier: ClassModifier? = null,
    val inheritanceModifier: InheritanceModifier? = null,
    val declarations: Int = 0,
    val classes: Int = 0,
    val functions: Int = 0,
    val properties: Int = 0,
    val aliases: Int = 0,
) {
    companion object {
        fun from(
            packageName: PackageName,
            clazz: ClassSignature,
        ): ClassStatistic =
            ClassStatistic(
                className = clazz.name,
                packageName = packageName,
                parameters = clazz.parameters.size,
                inheritedFrom = clazz.inheritedFrom.size,
                visibilityModifier = clazz.visibilityModifier,
                elementType = clazz.elementType,
                classModifier = clazz.classModifier,
                inheritanceModifier = clazz.inheritanceModifier,
                declarations = clazz.declarations.size,
                classes = clazz.classes.size,
                functions = clazz.functions.size,
                properties = clazz.properties.size,
                aliases = clazz.typeAliases.size,
            )
    }
}
