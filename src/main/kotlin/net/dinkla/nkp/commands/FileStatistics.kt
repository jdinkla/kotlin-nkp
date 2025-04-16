package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.fileStatistics
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class FileStatistics : CliktCommand() {
    override fun help(context: Context) = "File statistics"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = fileStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
