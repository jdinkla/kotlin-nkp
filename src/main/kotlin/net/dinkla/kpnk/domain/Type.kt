package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Type(val name: String?) {
    override fun toString(): String = name ?: ""
}
