package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.ClassModifier
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.InheritanceModifier
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.VisibilityModifier
import java.io.File

fun classStatistics(
    files: Files,
    outputFile: File,
) {
    val classes: List<Pair<PackageName, ClassSignature>> =
        files.map { file ->
            file.classes.map { clazz ->
                Pair(
                    file
                        .packageName,
                    clazz,
                )
            }
        }.flatten()
    val stats = classes.map { ClassStatistics.from(it.first, it.second) }
    logger.info { "Writing class statistics to ${outputFile.absolutePath}" }
    save(outputFile, stats)
}

@Serializable
internal data class ClassStatistics(
    val name: String,
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
        fun from(packageName: PackageName, clazz: ClassSignature): ClassStatistics {
            return ClassStatistics(
                name = clazz.name,
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
                aliases = clazz.aliases.size,
            )
        }
    }
}

private val logger = KotlinLogging.logger {}
