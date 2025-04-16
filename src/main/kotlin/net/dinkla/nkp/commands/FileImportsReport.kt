package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.allFileImports
import net.dinkla.nkp.analysis.filteredFileImports
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class FileImportsReport : AbstractCommand("Imports report") {
    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val imports =
            if (excludeOtherLibraries) {
                filteredFileImports(project)
            } else {
                allFileImports(project)
            }
        echo(Json.encodeToString(imports))
    }
}
