package net.dinkla.kpnk.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.kpnk.domain.Files

fun inheritance(files: Files) {
    logger.info { "*** Inheritance ***" }
    files.inheritance().sortedByDescending { it.second + it.third }.forEach {
        println(it)
    }
}

internal fun Files.inheritance(): List<Triple<String, Int, Int>> {
    return flatMap { file -> file.classes }.map {classSignature ->
        val h = this.searchHierarchy(classSignature.name)
        val l = this.searchImplementers(classSignature.name)
        Triple(classSignature.name, h.size - 1, l.size)
    }
}

private val logger = KotlinLogging.logger {}
