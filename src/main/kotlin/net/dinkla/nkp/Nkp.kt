package net.dinkla.nkp

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import net.dinkla.nkp.commands.ClassStatistics
import net.dinkla.nkp.commands.FileStatistics
import net.dinkla.nkp.commands.Inheritance
import net.dinkla.nkp.commands.MermaidClassDiagram
import net.dinkla.nkp.commands.MermaidImportDiagram
import net.dinkla.nkp.commands.Outliers
import net.dinkla.nkp.commands.PackageStatistics
import net.dinkla.nkp.commands.Packages
import net.dinkla.nkp.commands.Parse
import net.dinkla.nkp.commands.Search

class Nkp : CliktCommand(name = "nkp") {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    Nkp()
        .subcommands(
            Parse(),
            Inheritance(),
            Outliers(),
            ClassStatistics(),
            FileStatistics(),
            PackageStatistics(),
            MermaidClassDiagram(),
            MermaidImportDiagram(),
            Search(),
            Packages(),
        ).main(args)
}
