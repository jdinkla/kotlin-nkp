package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.ImportedElement
import net.dinkla.nkp.domain.kotlinlang.Package
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.analysis.AnalyzedPackage as AnalyzedPackage1

fun packagesStatistics(project: Project): List<AnalyzedPackage1> = AnalyzedPackage1.from(project)

@Serializable
data class AnalyzedPackage(
    val packageName: PackageName,
    val importedElements: Set<ImportedElement>,
    val importStatistics: ImportStatistics,
    val declarationStatistics: DeclarationStatistics,
) {
    companion object {
        fun from(project: Project): List<AnalyzedPackage1> =
            project.packages().map { from(it) }.sortedBy { it.packageName.name }

        fun from(p: Package) =
            AnalyzedPackage1(
                packageName = p.packageName,
                importedElements =
                    p
                        .imports()
                        .map { it.name }
                        .sortedBy { it.name }
                        .toSet(),
                importStatistics = ImportStatistics.from(p),
                declarationStatistics = DeclarationStatistics.from(p),
            )
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
                total = imports.distinctBy { it.name.name }.size,
                distinct = packages.size,
                fromSubPackage = packages.count { it.isSubPackageOf(p.packageName) },
                fromSuperPackage = packages.count { it.isSuperPackage(p.packageName) },
                fromSidePackage = packages.count { it.isSidePackage(p.packageName) },
                fromOtherPackage = packages.count { it.isOtherPackage(p.packageName) },
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
        fun from(p: Package) =
            DeclarationStatistics(
                files = p.files.size,
                functions = p.functions.size,
                properties = p.properties.size,
                classes = p.classes.size,
                typeAliases = p.typeAliases.size,
            )
    }
}
