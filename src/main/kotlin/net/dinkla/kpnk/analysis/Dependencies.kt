package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.PackageName
import net.dinkla.kpnk.logger
import java.io.File

fun dependencies(
    files: Files,
    file: File
) {
    val dependencies = Dependencies.from(files)
    logger.info("Writing dependencies to ${file.absolutePath}")
    val string = Json.encodeToString(dependencies)
    file.writeText(string)
}

@Serializable
data class Dependency(val name: PackageName, val dependencies: Set<ImportedElement>) {
    override fun toString(): String = "${name.name}: ${dependencies.joinToString(", ")}"
}

@Serializable
data class Dependencies(val dependencies: List<Dependency>) {
    companion object {
        fun from(files: Files): Dependencies = from(dependencyMap(files))
    }
}

private typealias InternalMap = Map<PackageName, Set<ImportedElement>>

private fun from(dependencies: InternalMap): Dependencies =
    Dependencies(
        dependencies.map { (key, value) ->
            Dependency(key, value)
        },
    )

private fun dependencyMap(files: Files): InternalMap {
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
