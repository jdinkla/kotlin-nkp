package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.Command
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.logger

object Inheritance : Command {
    override val description: String = "shows inheritance"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    ) {
        reportInheritance(fileInfos!!)
    }
}

internal fun reportInheritance(infos: FileInfos) {
    logger.info("*** Inheritance ***")
    infos.inheritance().sortedByDescending { it.second + it.third }.forEach {
        println(it)
    }
}

internal fun FileInfos.inheritance(): List<Triple<String, Int, Int>> {
    return flatMap { fileInfo -> fileInfo.topLevel.classes }.map {
        val h = this.searchHierarchy(it.name)
        val l = this.searchImplementers(it.name)
        Triple(it.name, h.size - 1, l.size)
    }
}
