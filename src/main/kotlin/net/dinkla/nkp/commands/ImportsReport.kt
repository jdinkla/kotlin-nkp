package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.allImports
import net.dinkla.nkp.analysis.filteredImports
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class ImportsReport : AbstractCommand("Imports report") {
    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val report =
            if (excludeOtherLibraries) {
                filteredImports(project)
            } else {
                allImports(project)
            }
        echo(Json.encodeToString(report))
    }
}
