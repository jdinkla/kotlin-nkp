package net.dinkla.nkp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import net.dinkla.nkp.commands.ClassStatisticsCommand
import net.dinkla.nkp.commands.FileStatisticsCommand
import net.dinkla.nkp.commands.MermaidClassDiagram
import net.dinkla.nkp.commands.MermaidCouplingDiagram
import net.dinkla.nkp.commands.MermaidImportDiagram
import net.dinkla.nkp.commands.PackageCouplingCommand
import net.dinkla.nkp.commands.PackageStatisticsCommand
import net.dinkla.nkp.commands.PackagesCommand
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
            FileStatisticsCommand(),
            MermaidClassDiagram(),
            MermaidCouplingDiagram(),
            MermaidImportDiagram(),
            PackageCouplingCommand(),
            PackageStatisticsCommand(),
            PackagesCommand(),
            SearchCommand(),
        ).main(args)
}
