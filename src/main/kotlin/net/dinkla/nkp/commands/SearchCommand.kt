package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.search
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadJson

class SearchCommand : AbstractCommand("Search for a class by name", "search") {
    private val className by argument(help = "class name")

    override fun run() {
        val project = model.loadJson<Project>()
        val result = project.search(className)
        echo(Json.encodeToString(result))
    }
}
