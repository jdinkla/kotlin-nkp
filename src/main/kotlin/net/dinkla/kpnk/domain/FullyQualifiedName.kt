package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name

    val packageName: String
        get() {
            val index = name.lastIndexOf(".")
            return if (index >= 0) {
                name.substring(0, index)
            } else {
                name
            }
        }
}
