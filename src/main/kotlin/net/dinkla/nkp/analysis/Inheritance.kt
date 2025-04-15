package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Project

@Serializable
data class Inheritance(
    val className: String,
    val occurrencesInHierarchy: Int,
    val numberOfImplementers: Int,
)

internal fun Project.inheritance(): List<Inheritance> =
    flatMap { it.classes }
        .map {
            val h = getInheritanceHierarchy(it.name)
            val l = getImplementationsOf(it.name)
            Inheritance(it.name, h.size - 1, l.size)
        }.sortedByDescending { it.occurrencesInHierarchy + it.numberOfImplementers }
