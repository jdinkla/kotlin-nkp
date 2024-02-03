package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class Package(val name: PackageName, val files: List<AnalysedFile>) {
    override fun toString(): String {
        return "${name.name}: ${files.joinToString(", ") { it.packageName() }}"
    }
}
