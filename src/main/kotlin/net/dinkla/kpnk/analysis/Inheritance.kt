package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.kpnk.domain.Files

fun inheritance(infos: Files) {
    logger.info { "*** Inheritance ***" }
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

private val logger = KotlinLogging.logger {}
