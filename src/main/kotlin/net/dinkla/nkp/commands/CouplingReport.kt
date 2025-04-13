package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.combinedReport
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.loadFromJsonFile

class CouplingReport : CliktCommand() {
    override fun help(context: Context) = "Generate package coupling metrics"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

    override fun run() {
        val files = loadFromJsonFile<Files>(model.absolutePath)
        val report = combinedReport(files, excludeOtherLibraries)
        echo(Json.encodeToString(report))
    }
}
