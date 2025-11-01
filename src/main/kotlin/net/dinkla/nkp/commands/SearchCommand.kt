package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.search

class SearchCommand : AbstractCommand("Search for a class by name", "search") {
    private val className by argument(help = "class name")

    override fun run() {
        val project = loadProject()
        val result = project.search(className)
        echo(Json.encodeToString(result))
    }
}
