package net.dinkla.nkp.analysis

import net.dinkla.nkp.domain.Files

internal fun Files.inheritance(): List<Triple<String, Int, Int>> =
    flatMap { file -> file.classes }.map { classSignature ->
        val h = this.searchHierarchy(classSignature.name)
        val l = this.searchImplementers(classSignature.name)
        Triple(classSignature.name, h.size - 1, l.size)
    }.sortedByDescending { it.second + it.third }
