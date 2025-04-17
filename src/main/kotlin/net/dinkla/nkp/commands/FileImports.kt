package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.DeclarationFilter
import net.dinkla.nkp.analysis.ImportFilter
import net.dinkla.nkp.analysis.fileImports
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class FileImports : AbstractCommand("Imports report") {
    private val includeAllLibraries by option(help = "include all libraries").flag(default = false)

    private val includePrivateDeclarations by option(help = "include private declarations").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val declarationFilter = DeclarationFilter.select(includePrivateDeclarations)
        val importFilter = ImportFilter.select(includeAllLibraries)
        val imports = fileImports(project, declarationFilter, importFilter)
        echo(Json.encodeToString(imports))
    }
}
