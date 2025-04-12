package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Files

@Serializable
data class Inheritance(
    val className: String,
    val occurencesInHierarchy: Int,
    val numberOfImplementers: Int,
)

internal fun Files.inheritance(): List<Inheritance> =
    flatMap { file -> file.classes }
        .map { classSignature ->
            val h = this.searchHierarchy(classSignature.name)
            val l = this.searchImplementers(classSignature.name)
            Inheritance(classSignature.name, h.size - 1, l.size)
        }.sortedByDescending { it.occurencesInHierarchy + it.numberOfImplementers }
