package net.dinkla.kpnk.analyze

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.elements.FileInfo

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
    for (info in infos) {
        val name = info.packageName()
        for (imp in info.elements.imports) {
            val packageName = imp.packageName()
            val set = dependencies.getOrDefault(name, mutableSetOf())
            set += packageName
            dependencies[name] = set
        }
    }
    return dependencies.mapValues { it.value.toSet() }
}
