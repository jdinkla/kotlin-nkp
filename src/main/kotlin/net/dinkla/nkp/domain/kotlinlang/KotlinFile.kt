package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.FilePath

@Serializable
data class KotlinFile(
    val filePath: FilePath,
    val packageName: PackageName,
    val imports: List<Import> = listOf(),
    override val declarations: List<Declaration> = listOf(),
) : DeclarationContainer {
    val fullyQualifiedName: String
        get() {
            val name = filePath.fileName.replace(".kt", "")
            return "${packageName.name}.$name"
        }
}
