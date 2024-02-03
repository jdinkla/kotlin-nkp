package net.dinkla.kpnk

import net.dinkla.kpnk.analysis.DependenciesCommand
import net.dinkla.kpnk.analysis.Inheritance
import net.dinkla.kpnk.analysis.MermaidClassDiagram
import net.dinkla.kpnk.analysis.Outliers
import net.dinkla.kpnk.analysis.Search
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.command.SaveCommand
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.Files
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

internal val logger: Logger = LoggerFactory.getLogger("Main")

private val commands =
    listOf(
        "dependencies" to DependenciesCommand,
        "inheritance" to Inheritance,
        "outliers" to Outliers,
        "search" to Search,
        "save" to SaveCommand,
        "mermaid-class-diagram" to MermaidClassDiagram,
    )

fun main(args: Array<String>) {
    commands.forEach { (name, command) ->
        CommandManager.add(name, command)
    }
    if (args.size < 2) {
        CommandManager.synopsis()
        exitProcess(-1)
    }
    val command = CommandManager.get(args[1])
    if (command == null) {
        CommandManager.synopsis()
        exitProcess(-1)
    } else {
        val infos: Files = read(args[0])
        command.execute(args.drop(2).toTypedArray(), infos)
    }
}

private fun read(fileName: String): Files {
    val file = File(fileName)
    return if (file.isDirectory) {
        FileInfo.readFromDirectory(file.absolutePath)
    } else {
        FileInfo.loadFromJsonFile(file.absolutePath)
    }
}
