package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.ImportedElement
import net.dinkla.nkp.domain.Package
import net.dinkla.nkp.domain.PackageName
import java.io.File

fun packageStatistics(files: Files): List<AnalyzedPackage> = AnalyzedPackage.from(files)

fun packageStatistics(
    files: Files,
    outputFile: File,
) {
    logger.info { "Writing import statistics to ${outputFile.absolutePath}" }
    save(outputFile, packageStatistics(files))
}

@Serializable
data class AnalyzedPackage(
    val packageName: PackageName,
    val importedElements: Set<ImportedElement>,
    val importStatistics: ImportStatistics,
    val declarationStatistics: DeclarationStatistics,
) {
    companion object {
        fun from(files: Files): List<AnalyzedPackage> =
            files.packages().map { from(it) }.sortedBy { it.packageName.name }

        fun from(p: Package): AnalyzedPackage {
            val name = p.packageName
            val elements = p.imports().map { it.name }.sortedBy { it.name }
            val importStatistics = ImportStatistics.from(p)
            val declarationStatistics = DeclarationStatistics.from(p)
            return AnalyzedPackage(name, elements.toSet(), importStatistics, declarationStatistics)
        }
    }
}

@Serializable
data class ImportStatistics(
    val total: Int,
    val distinct: Int,
    val fromSubPackage: Int,
    val fromSuperPackage: Int,
    val fromSidePackage: Int,
    val fromOtherPackage: Int,
) {
    companion object {
        fun from(p: Package): ImportStatistics {
            val imports = p.imports()
            val packages = imports.map { it.name.packageName }.toSet()
            return ImportStatistics(
                imports.distinctBy { it.name.name }.size,
                packages.size,
                packages.count { it.isSubPackageOf(p.packageName) },
                packages.count { it.isSuperPackage(p.packageName) },
                packages.count { it.isSidePackage(p.packageName) },
                packages.count { it.isOtherPackage(p.packageName) },
            )
        }
    }
}

@Serializable
data class DeclarationStatistics(
    val files: Int,
    val functions: Int,
    val properties: Int,
    val classes: Int,
    val typeAliases: Int,
) {
    companion object {
        fun from(p: Package): DeclarationStatistics {
            val files = p.files.size
            val functions = p.functions.size
            val properties = p.properties.size
            val classes = p.classes.size
            val typeAliases = p.typeAliases.size
            return DeclarationStatistics(files, functions, properties, classes, typeAliases)
        }
    }
}

private val logger = KotlinLogging.logger {}
