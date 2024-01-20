package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class Parameter(val name: String, val type: Type)

fun Parameter.prettyPrint(): String = "$name: $type"
