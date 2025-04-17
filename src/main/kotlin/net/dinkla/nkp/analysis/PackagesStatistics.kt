package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.ImportedElement
import net.dinkla.nkp.domain.kotlinlang.Package
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project

fun packagesStatistics(project: Project): List<AnalyzedPackage> = AnalyzedPackage.from(project)

@Serializable
data class AnalyzedPackage(
    val packageName: PackageName,
    val importedElements: Set<ImportedElement>,
    val importStatistics: ImportStatistics,
    val declarationStatistics: DeclarationStatistics,
) {
    companion object {
        fun from(project: Project): List<AnalyzedPackage> =
            project.packages().map { from(it) }.sortedBy { it.packageName.name }

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
