package net.dinkla.kpnk

import kotlinx.serialization.Serializable

@Serializable
data class Dependency(val name: String, val dependencies: Set<String>)

@Serializable
data class Dependencies(val dependencies: List<Dependency>) {
    companion object {
        fun from(dependencies: Map<String, Set<String>>): Dependencies =
            Dependencies(
                dependencies.map { (key, value) ->
                    Dependency(key, value)
                },
            )
    }
}

fun dependencies(infos: List<FileInfo>): Map<String, Set<String>> {
    val dependencies = mutableMapOf<String, MutableSet<String>>()
    val parsed = infos.filterIsInstance<FileInfo.Parsed>()
    for (info in parsed) {
        val name = info.basename()
        for (imp in info.elements.imports) {
            val packageName = imp.packageName()
            val set = dependencies.getOrDefault(name, mutableSetOf())
            set += packageName
            dependencies[name] = set
        }
    }
    return dependencies.mapValues { it.value.toSet() }
}
