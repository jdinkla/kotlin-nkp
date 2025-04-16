package net.dinkla.nkp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import net.dinkla.nkp.commands.ClassStatistics
import net.dinkla.nkp.commands.CouplingReport
import net.dinkla.nkp.commands.FileImportsReport
import net.dinkla.nkp.commands.FileStatistics
import net.dinkla.nkp.commands.ImportsReport
import net.dinkla.nkp.commands.InheritanceReport
import net.dinkla.nkp.commands.MermaidClassDiagram
import net.dinkla.nkp.commands.MermaidCouplingDiagram
import net.dinkla.nkp.commands.MermaidImportDiagram
import net.dinkla.nkp.commands.OutlierReport
import net.dinkla.nkp.commands.PackagesStatistics
import net.dinkla.nkp.commands.PackagesReport
import net.dinkla.nkp.commands.Parse
import net.dinkla.nkp.commands.SearchReport

class Nkp : CliktCommand(name = "nkp") {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    Nkp()
        .subcommands(
            Parse(),
            ClassStatistics(),
            CouplingReport(),
            FileImportsReport(),
            FileStatistics(),
            ImportsReport(),
            InheritanceReport(),
            OutlierReport(),
            PackagesReport(),
            PackagesStatistics(),
            SearchReport(),
            MermaidClassDiagram(),
            MermaidCouplingDiagram(),
            MermaidImportDiagram(),
        ).main(args)
}
