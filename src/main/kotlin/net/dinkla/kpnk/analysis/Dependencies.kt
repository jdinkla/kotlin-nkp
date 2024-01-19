package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.Command
import net.dinkla.kpnk.CommandManager
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.FileInfos
import java.io.File

object DependenciesCommand : Command {
    override val description: String = "reports dependencies to stdout or to a file with --output <filename>"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    ) {
        val dependencies = Dependencies.from(dependencies(fileInfos!!))
        val string = Json.encodeToString(dependencies)
        if (args.size == 2 && args[0] == "--output") {
            val filename = args[1]
            File(filename).writeText(string)
        } else if (args.isEmpty()) {
            println(string)
        } else {
            CommandManager.synopsis()
        }
    }
}

@Serializable
internal data class Dependency(val name: String, val dependencies: Set<String>)

@Serializable
internal data class Dependencies(val dependencies: List<Dependency>) {
    companion object {
        fun from(dependencies: Map<String, Set<String>>): Dependencies =
            Dependencies(
                dependencies.map { (key, value) ->
                    Dependency(key, value)
                },
            )
    }
}

internal fun dependencies(infos: List<FileInfo>): Map<String, Set<String>> {
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
