package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.domain.FileInfo

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
        for (imp in info.topLevel.imports) {
            val set = dependencies.getOrDefault(name, mutableSetOf())
            set += imp.packageName
            dependencies[name] = set
        }
    }
    return dependencies.mapValues { it.value.toSet() }
}
