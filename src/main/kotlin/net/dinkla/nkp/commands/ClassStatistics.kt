package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.classStatistics
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.loadFromJsonFile

class ClassStatistics : CliktCommand() {
    override fun help(context: Context) = "Class statistics"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    override fun run() {
        val files = loadFromJsonFile<Files>(model.absolutePath)
        val stats = classStatistics(files)
        echo(Json.encodeToString(stats))
    }
}
