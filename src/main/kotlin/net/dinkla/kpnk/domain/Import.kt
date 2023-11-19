package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class Import(val name: FullyQualifiedName) {
    val packageName: String
        get() = name.packageName
}
