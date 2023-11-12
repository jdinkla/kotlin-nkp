package net.dinkla.kpnk.elements

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name
}

@Serializable
sealed interface FileInfo {
    @Serializable
    data class Parsed(
        val fileName: String,
        val elements: Elements,
    ) : FileInfo {
        fun basename(): String {
            val name = net.dinkla.kpnk.basename(fileName).replace(".kt", "")
            return elements.packageName.toString() + "." + name
        }
    }

    @Serializable
    data class Error(val fileName: String, val message: String) : FileInfo
}

@Serializable
data class Elements(
    val packageName: FullyQualifiedName,
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val classes: List<ClassSignature> = listOf(),
)

@Serializable
data class Import(val name: FullyQualifiedName) {
    fun packageName(): String {
        val name = name.toString()
        val index = name.lastIndexOf(".")
        return if (index >= 0) {
            name.substring(0, index)
        } else {
            name
        }
    }
}

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
