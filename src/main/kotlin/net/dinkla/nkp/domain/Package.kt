package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Package(
    val packageName: PackageName,
    val files: List<AnalysedFile>,
) : HasDeclarations {
    override val declarations: List<Defined>
        get() = files.flatMap { it.declarations }

    fun imports(): Set<Import> = files.flatMap { it.imports }.toSet()

    override fun toString(): String {
        return "${packageName.name}: ${files.joinToString(", ") { it.packageName() }}"
    }
}
