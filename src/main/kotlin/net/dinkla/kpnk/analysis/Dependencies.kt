package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.PackageName

@Serializable
data class Dependency(val name: PackageName, val dependencies: Set<ImportedElement>) {
    override fun toString(): String = "${name.name}: ${dependencies.joinToString(", ")}"
}

@Serializable
data class Dependencies(val dependencies: List<Dependency>) {
    companion object {
        fun from(files: Files): Dependencies = from(dependencies(files))
    }
}

private typealias InternalMap = Map<PackageName, Set<ImportedElement>>

private fun from(dependencies: InternalMap): Dependencies =
    Dependencies(
        dependencies.map { (key, value) ->
            Dependency(key, value)
        },
    )

private fun dependencies(files: Files): InternalMap {
    val dependencies = mutableMapOf<PackageName, MutableSet<ImportedElement>>()
    for (file in files) {
        val packageName = file.packageName
        for (imp in file.imports) {
            val set = dependencies.getOrDefault(packageName, mutableSetOf())
            set += imp.name
            dependencies[packageName] = set
        }
    }
    return dependencies.mapValues { it.value.toSet() }
}
