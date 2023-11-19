package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.utilities.FileName

@Serializable
data class FileInfo(
    val fileName: FileName,
    val topLevel: TopLevel,
) {
    fun packageName(): String {
        val name = fileName.basename.replace(".kt", "")
        return topLevel.packageName.toString() + "." + name
    }
}

@Serializable
data class TopLevel(
    val packageName: FullyQualifiedName,
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val classes: List<ClassSignature> = listOf(),
    val typeAliases: List<TypeAlias> = listOf(),
    val properties: List<Property> = listOf(),
)

@Serializable
data class Parameter(val name: String, val type: String)

@Serializable
enum class VisibilityModifier(val text: String) {
    PUBLIC(""),
    PRIVATE("private"),
    INTERNAL("internal"),
    PROTECTED("protected"),
}

@Serializable
enum class ClassModifier(val text: String) {
    DATA("data"),
    ENUM("enum"),
    VALUE("value "),
}

@Serializable
enum class InheritanceModifier(val text: String) {
    OPEN("open"),
    ABSTRACT("abstract"),
}

@Serializable
enum class Type(val text: String) {
    CLASS("class"),
    OBJECT("object"),
    INTERFACE("interface"),
}

@Serializable
sealed interface Defined

@Serializable
data class TypeAlias(val name: String, val def: String) : Defined

@Serializable
data class Property(
    val name: String,
    val dataType: String?,
    val modifier: PropertyModifier = PropertyModifier.VAL,
) : Defined
