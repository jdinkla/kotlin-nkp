package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.Package
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project

@Serializable
data class PackageImports(
    val packageName: PackageName,
    val imports: Set<PackageName>,
) {
    companion object {
        fun from(p: Package) =
            PackageImports(
                packageName = p.packageName,
                imports = p.imports().map { it.name.packageName }.toSortedSet(compareBy { it.name }),
            )

        private fun fromFiltered(p: Package) =
            PackageImports(
                packageName = p.packageName,
                imports =
                    p
                        .imports()
                        .filter {
                            !it.name.packageName.isOtherPackage(p.packageName)
                        }.map { it.name.packageName }
                        .toSortedSet(compareBy { it.name }),
            )

        fun filteredImports(project: Project): List<PackageImports> = project.packages().map { fromFiltered(it) }

        fun allImports(project: Project): List<PackageImports> = project.packages().map { from(it) }
    }
}
