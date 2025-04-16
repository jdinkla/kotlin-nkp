package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.search
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class SearchReport : AbstractCommand("Search class") {
    private val className by argument(help = "class name")

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val result = project.search(className)
        echo(Json.encodeToString(result))
    }
}
