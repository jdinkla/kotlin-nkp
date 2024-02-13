package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.Package
import net.dinkla.kpnk.domain.PackageName
import java.io.File


fun importStatistics(
    files: Files,
    file: File,
) {
    save(file, AnalyzedPackage.from(files))
}

@Serializable
internal data class AnalyzedPackage(
    val packageName: PackageName,
    val importedElements: Set<ImportedElement>,
    val importStatistics: ImportStatistics,
) {
    companion object {
        fun from(files: Files): List<AnalyzedPackage> =
            files.packages().map { from(it) }.sortedBy { it.packageName.name }

        fun from(p: Package): AnalyzedPackage {
            val name = p.packageName
            val elements = p.imports().map { it.name }.sortedBy { it.name }
            val stats = ImportStatistics.from(p)
            return AnalyzedPackage(name, elements.toSet(), stats)
        }
    }
}

@Serializable
internal data class ImportStatistics(
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

private fun save(
    file: File,
    analyzedPackages: List<AnalyzedPackage>,
) {
    val string = Json.encodeToString(analyzedPackages)
    logger.info { "Writing import statistics to ${file.absolutePath}" }
    file.writeText(string)
}

private val logger = KotlinLogging.logger {}
