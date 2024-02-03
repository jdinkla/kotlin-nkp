package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class AnalysedFile(
    val fileName: FileName,
    val packageName: PackageName,
    val imports: List<Import> = listOf(),
    override val declarations: List<Defined> = listOf(),
) : HasDeclarations {
    fun packageName(): String {
        val name = fileName.basename.replace(".kt", "")
        return "${packageName.name}.$name"
    }
}
