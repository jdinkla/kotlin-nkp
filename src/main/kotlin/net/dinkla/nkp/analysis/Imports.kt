package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Package
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Project

fun imports(
    project: Project,
    excludeOtherLibraries: Boolean,
): List<Imports> =
    if (excludeOtherLibraries) {
        project.packages().map { Imports.fromFiltered(it) }
    } else {
        project.packages().map { Imports.from(it) }
    }

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
