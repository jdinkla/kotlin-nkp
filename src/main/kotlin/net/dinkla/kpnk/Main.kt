package net.dinkla.kpnk

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.kpnk.analysis.debug
import net.dinkla.kpnk.analysis.details
import net.dinkla.kpnk.analysis.importStatistics
import net.dinkla.kpnk.analysis.inheritance
import net.dinkla.kpnk.analysis.mermaidClassDiagram
import net.dinkla.kpnk.analysis.outliers
import net.dinkla.kpnk.analysis.overview
import net.dinkla.kpnk.analysis.packages
import net.dinkla.kpnk.analysis.search
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.utilities.read
import net.dinkla.kpnk.utilities.saveToJsonFile

class Main : CliktCommand() {
    private val source by argument().file(mustExist = true, canBeDir = true, canBeFile = true)
    private val debug by option(help = "debug").flag()
    private val importStatistics by file(help = "analyze imports for every package")
    private val mermaidClassDiagram by file(
        help = "Generate a mermaid class diagram (.mermaid or .html)",
    )
    private val save by file("save parsed source code as json")

    private val inheritance by file()
    private val outliers by file()
    private val search by option()
    private val details by file()
    private val packages by file(help = "save as json")
    private val overview by file(help = "overview")

    private fun file(help: String = "") = option(help = help).file(canBeDir = false)

    override fun run() {
        val files: Files = Files.read(source)
        if (save != null) {
            files.saveToJsonFile(save!!.absolutePath)
        }
        if (inheritance != null) {
            inheritance(files)
        }
        if (outliers != null) {
            outliers(files)
        }
        if (search != null) {
            search(files, search!!)
        }
        if (mermaidClassDiagram != null) {
            mermaidClassDiagram(files, mermaidClassDiagram!!)
        }
        if (details != null) {
            details(files)
        }
        if (packages != null) {
            packages(files, packages!!)
        }
        if (importStatistics != null) {
            importStatistics(files, importStatistics!!)
        }
        if (overview != null) {
            overview(files, overview!!)
        }
        if (debug) {
            debug(files)
        }
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}
