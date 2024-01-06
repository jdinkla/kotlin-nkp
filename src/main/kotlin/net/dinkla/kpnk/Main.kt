package net.dinkla.kpnk

import net.dinkla.kpnk.analysis.DependenciesCommand
import net.dinkla.kpnk.analysis.Inheritance
import net.dinkla.kpnk.analysis.Outliers
import net.dinkla.kpnk.analysis.Search
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.domain.readFromDirectory
import net.dinkla.kpnk.utilities.loadFromJsonFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

val logger: Logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) {
    CommandManager.add("dependencies", DependenciesCommand)
    CommandManager.add("inheritance", Inheritance)
    CommandManager.add("outliers", Outliers)
    CommandManager.add("search", Search)
    if (args.size < 2) {
        CommandManager.synopsis()
        exitProcess(-1)
    }
    val command = CommandManager.get(args[1])
    if (command == null) {
        CommandManager.synopsis()
        exitProcess(-1)
    } else {
        val infos = read(args[0])
        command.execute(args.drop(2).toTypedArray(), infos)
    }
}

private fun read(fileName: String): FileInfos {
    val file = File(fileName)
    return if (file.isDirectory) {
        readFromDirectory(file.absolutePath)
    } else {
        loadFromJsonFile(file.absolutePath)
    }
}
