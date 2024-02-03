package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package
import java.io.File

object ImportsCommand : Command {
    override val description: String = "reports details about imports to stdout or to a file with --output <filename>"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        val packages = files.packages()
        val imports = imports(packages)
        if (args.size == 2 && args[0] == "--output") {
            val filename = args[1]
            val string = Json.encodeToString(imports)
            File(filename).writeText(string)
        } else if (args.isEmpty()) {
            println(imports.joinToString("\n"))
        } else {
            CommandManager.synopsis()
        }
    }
}

@Serializable
private data class Stats(
    val name: String,
    val files: Int,
    val functions: Int,
    val properties: Int,
    val classes: Int,
    val typeAliases: Int,
) {
    override fun toString(): String =
        "$name, $files files, $classes classes $functions functions, $properties properties, $typeAliases type aliases"

    companion object {
        fun from(p: Package): Stats {
            val name = p.packageName.name
            val files = p.files.size
            val functions = p.functions.size
            val properties = p.properties.size
            val classes = p.classes.size
            val typeAliases = p.typeAliases.size
            return Stats(name, files, functions, properties, classes, typeAliases)
        }
    }
}

private fun imports(packages: List<Package>): List<Stats> = packages.map { Stats.from(it) }
