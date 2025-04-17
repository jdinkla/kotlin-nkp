package net.dinkla.nkp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import net.dinkla.nkp.commands.ClassStatisticsCommand
import net.dinkla.nkp.commands.CouplingReport
import net.dinkla.nkp.commands.FileImports
import net.dinkla.nkp.commands.FileStatistics
import net.dinkla.nkp.commands.MermaidClassDiagram
import net.dinkla.nkp.commands.MermaidCouplingDiagram
import net.dinkla.nkp.commands.MermaidImportDiagram
import net.dinkla.nkp.commands.PackagesImports
import net.dinkla.nkp.commands.PackagesReport
import net.dinkla.nkp.commands.PackagesStatistics
import net.dinkla.nkp.commands.Parse
import net.dinkla.nkp.commands.SearchCommand

class Nkp : CliktCommand(name = "nkp") {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    Nkp()
        .subcommands(
            Parse(),
            ClassStatisticsCommand(),
            CouplingReport(),
            FileImports(),
            FileStatistics(),
            PackagesImports(),
            PackagesReport(),
            PackagesStatistics(),
            SearchCommand(),
            MermaidClassDiagram(),
            MermaidCouplingDiagram(),
            MermaidImportDiagram(),
        ).main(args)
}
