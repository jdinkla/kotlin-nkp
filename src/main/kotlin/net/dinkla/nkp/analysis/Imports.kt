package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.Package
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.domain.kotlinlang.Project

fun filteredImports(project: Project): List<Imports> = project.packages().map { Imports.fromFiltered(it) }

fun allImports(project: Project): List<Imports> = project.packages().map { Imports.from(it) }

@Serializable
data class Imports(
    val packageName: PackageName,
    val imports: Set<PackageName>,
) {
    companion object {
        fun from(p: Package): Imports =
            Imports(
                packageName = p.packageName,
                imports = p.imports().map { it.name.packageName }.toSortedSet(compareBy { it.name }),
            )

        fun fromFiltered(p: Package): Imports =
            Imports(
                packageName = p.packageName,
                imports =
                    p
                        .imports()
                        .filter {
                            !it.name.packageName.isOtherPackage(p.packageName)
                        }.map { it.name.packageName }
                        .toSortedSet(compareBy { it.name }),
            )
    }
}
