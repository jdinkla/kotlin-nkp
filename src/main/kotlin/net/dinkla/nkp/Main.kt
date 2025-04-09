package net.dinkla.nkp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.nkp.analysis.classStatistics
import net.dinkla.nkp.analysis.debug
import net.dinkla.nkp.analysis.fileStatistics
import net.dinkla.nkp.analysis.inheritance
import net.dinkla.nkp.analysis.mermaidClassDiagram
import net.dinkla.nkp.analysis.mermaidImportsFlowDiagram
import net.dinkla.nkp.analysis.outliers
import net.dinkla.nkp.analysis.packageStatistics
import net.dinkla.nkp.analysis.packages
import net.dinkla.nkp.analysis.save
import net.dinkla.nkp.analysis.search
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.read
import net.dinkla.nkp.utilities.saveToJsonFile

class Main : CliktCommand() {
    private val source by argument().file(mustExist = true, canBeDir = true, canBeFile = true)
    private val classStatistics by file(help = "statistics on class level")
    private val debug by option(help = "debug").flag()
    private val fileStatistics by file(help = "statistics for all files")
    private val mermaidClassDiagram by file(
        help = "Generate a mermaid class diagram (.mermaid or .html)",
    )
    private val mermaidImportsFlowDiagram by file(
        help = "Generate mermaid flow diagram for imports (.mermaid or .html)",
    )
    private val packages by file(help = "exports all information organized by packages")
    private val packageStatistics by file(help = "analysis for all packages")
    private val save by file("save parsed source code as json")

    private val inheritance by file()
    private val outliers by file()
    private val search by option()

    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

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
            mermaidClassDiagram(files).save(mermaidClassDiagram!!)
        }
        if (mermaidImportsFlowDiagram != null) {
            mermaidImportsFlowDiagram(files, excludeOtherLibraries).save(mermaidImportsFlowDiagram!!)
        }
        if (fileStatistics != null) {
            fileStatistics(files, fileStatistics!!)
        }
        if (packages != null) {
            packages(files, packages!!)
        }
        if (packageStatistics != null) {
            packageStatistics(files, packageStatistics!!)
        }
        if (classStatistics != null) {
            classStatistics(files, classStatistics!!)
        }
        if (debug) {
            debug(files)
        }
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}
