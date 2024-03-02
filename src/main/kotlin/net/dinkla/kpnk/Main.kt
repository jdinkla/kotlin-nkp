package net.dinkla.kpnk

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.kpnk.analysis.classStatistics
import net.dinkla.kpnk.analysis.debug
import net.dinkla.kpnk.analysis.fileStatistics
import net.dinkla.kpnk.analysis.packageStatistics
import net.dinkla.kpnk.analysis.inheritance
import net.dinkla.kpnk.analysis.mermaidClassDiagram
import net.dinkla.kpnk.analysis.mermaidImportsFlowDiagram
import net.dinkla.kpnk.analysis.outliers
import net.dinkla.kpnk.analysis.packages
import net.dinkla.kpnk.analysis.search
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.utilities.read
import net.dinkla.kpnk.utilities.saveToJsonFile

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
        if (mermaidImportsFlowDiagram != null) {
            mermaidImportsFlowDiagram(files, mermaidImportsFlowDiagram!!)
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
