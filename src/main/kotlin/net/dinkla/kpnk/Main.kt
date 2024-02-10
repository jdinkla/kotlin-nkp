package net.dinkla.kpnk

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.kpnk.analysis.debug
import net.dinkla.kpnk.analysis.dependencies
import net.dinkla.kpnk.analysis.details
import net.dinkla.kpnk.analysis.importStatistics
import net.dinkla.kpnk.analysis.inheritance
import net.dinkla.kpnk.analysis.mermaidClassDiagram
import net.dinkla.kpnk.analysis.outliers
import net.dinkla.kpnk.analysis.overview
import net.dinkla.kpnk.analysis.packages
import net.dinkla.kpnk.analysis.search
import net.dinkla.kpnk.domain.Files
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

internal val logger: Logger = LoggerFactory.getLogger("Main")

class Nkp : CliktCommand() {
    private val source by argument().file(mustExist = true, canBeDir = true, canBeFile = true)
    private val save by option(help = "save parsed source code as json").file(canBeDir = false)
    private val dependencies by option(help = "save as json").file(canBeDir = false)
    private val inheritance by option().file(canBeDir = false)
    private val outliers by option().file(canBeDir = false)
    private val search by option()
    private val mermaidClassDiagram by option(
        help = "Generate a mermaid class diagram (.mermaid or .html)",
    ).file(
        canBeDir = false,
    )
    private val details by option().file(canBeDir = false)
    private val packages by option(help = "save as json").file(canBeDir = false)
    private val importStatistics by option(help = "save as json").file(canBeDir = false)
    private val overview by option(help = "save as json").file(canBeDir = false)
    private val debug by option(help = "debug").flag()

    override fun run() {
        logger.info("Reading from ${source.absolutePath}")
        val files: Files = read(source)
        if (save != null) {
            files.saveToJsonFile(save!!.absolutePath)
        }
        if (dependencies != null) {
            dependencies(files, dependencies!!)
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
    Nkp().main(args)
}

private fun read(file: File): Files =
    if (file.isDirectory) {
        Files.readFromDirectory(file.absolutePath)
    } else {
        Files.loadFromJsonFile(file.absolutePath)
    }
