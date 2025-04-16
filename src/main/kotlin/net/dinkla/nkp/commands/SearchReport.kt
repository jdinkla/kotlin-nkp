package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.search
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class SearchReport : CliktCommand() {
    override fun help(context: Context) = "Search class"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    private val className by argument(help = "class name")

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val result = project.search(className)
        echo(Json.encodeToString(result))
    }
}
