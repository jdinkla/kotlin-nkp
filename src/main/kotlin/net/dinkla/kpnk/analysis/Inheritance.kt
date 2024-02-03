package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.logger

object Inheritance : Command {
    override val description: String = "shows inheritance"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        reportInheritance(files)
    }
}

internal fun reportInheritance(infos: Files) {
    logger.info("*** Inheritance ***")
    infos.inheritance().sortedByDescending { it.second + it.third }.forEach {
        println(it)
    }
}

internal fun Files.inheritance(): List<Triple<String, Int, Int>> {
    return flatMap { file -> file.classes }.map {
        val h = this.searchHierarchy(it.name)
        val l = this.searchImplementers(it.name)
        Triple(it.name, h.size - 1, l.size)
    }
}
