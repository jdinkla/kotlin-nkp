package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class AnalysedFile(
    val fileName: FileName,
    val packageName: PackageName,
    val imports: List<Import> = listOf(),
    val declarations: List<Defined> = listOf(),
) {
    val functions: List<FunctionSignature>
        get() = declarations.filterIsInstance<FunctionSignature>()

    val properties: List<Property>
        get() = declarations.filterIsInstance<Property>()

    val classes: List<ClassSignature>
        get() = declarations.filterIsInstance<ClassSignature>()

    val typeAliases: List<TypeAlias>
        get() = declarations.filterIsInstance<TypeAlias>()

    fun packageName(): String {
        val name = fileName.basename.replace(".kt", "")
        return "${packageName.name}.$name"
    }
}
