package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class TypeAlias(val name: String, val def: Type) : Defined
