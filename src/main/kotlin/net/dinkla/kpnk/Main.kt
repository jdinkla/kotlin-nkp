package net.dinkla.kpnk

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.kpnk.analysis.DependenciesCommand
import net.dinkla.kpnk.analysis.DetailsCommand
import net.dinkla.kpnk.analysis.ImportStatsCommand
import net.dinkla.kpnk.analysis.Inheritance
import net.dinkla.kpnk.analysis.MermaidClassDiagram
import net.dinkla.kpnk.analysis.Outliers
import net.dinkla.kpnk.analysis.OverviewCommand
import net.dinkla.kpnk.analysis.PackagesCommand
import net.dinkla.kpnk.analysis.Search
import net.dinkla.kpnk.domain.Files
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

internal val logger: Logger = LoggerFactory.getLogger("Main")

class Nkp : CliktCommand() {
    val source by argument().file(mustExist = true, canBeDir = true, canBeFile = true)
    val save by option(help = "save parsed source code information to file").file(canBeDir = false)
    val dependencies by option().file(canBeDir = false)
    val inheritance by option().file(canBeDir = false)
    val outliers by option().file(canBeDir = false)
    val search by option()
    val mermaidClassDiagram by option(
        help = "Generate a mermaid class diagram (.mermaid or .html)",
    ).file(
        canBeDir = false,
    )
    val details by option().file(canBeDir = false)
    val packages by option().file(canBeDir = false)
    val importStatistics by option().file(canBeDir = false)
    val overview by option().file(canBeDir = false)

    override fun run() {
        logger.info("Reading from ${source.absolutePath}")
        val files: Files = read(source)
        if (save != null) {
            files.saveToJsonFile(save!!.absolutePath)
        }
        if (dependencies != null) {
            DependenciesCommand.execute(files, dependencies!!)
        }
        if (inheritance != null) {
            Inheritance.execute(files)
        }
        if (outliers != null) {
            Outliers.execute(files)
        }
        if (search != null) {
            Search.execute(files, search!!)
        }
        if (mermaidClassDiagram != null) {
            MermaidClassDiagram.execute(files, mermaidClassDiagram!!)
        }
        if (details != null) {
            DetailsCommand.execute(files)
        }
        if (packages != null) {
            PackagesCommand.execute(files, packages!!)
        }
        if (importStatistics != null) {
            ImportStatsCommand.execute(files, importStatistics!!)
        }
        if (overview != null) {
            OverviewCommand.execute(files, overview!!)
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
