package net.dinkla.kpnk

@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name
}

interface PrettyPrint {
    fun prettyPrint(): String
}

sealed interface FileInfo {
    data class Parsed(
        val fileName: String,
        val elements: Elements,
    ) : FileInfo {
        fun basename(): String {
            val name = basename(fileName).replace(".kt", "")
            return elements.packageName.toString() + "." + name
        }
    }

    data class Error(val fileName: String, val message: String) : FileInfo
}

data class Elements(
    val packageName: FullyQualifiedName,
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val classes: List<ClassSignature> = listOf(),
) : PrettyPrint {
    override fun prettyPrint(): String = """
package $packageName
${imports.joinToString("\n") { it.prettyPrint() }}
${functions.joinToString("\n") { it.prettyPrint() }}
${classes.joinToString("\n") { it.prettyPrint() }}
    """.trimIndent()
}

data class Import(val name: FullyQualifiedName) : PrettyPrint {
    override fun prettyPrint(): String = "import $name"
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

data class Parameter(val name: String, val type: String) : PrettyPrint {
    override fun prettyPrint(): String = "$name: $type"
}

data class FunctionSignature(
    val name: String,
    val returnType: String? = null,
    val parameters: List<Parameter> = listOf(),
    val extensionOf: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
) : PrettyPrint {
    override fun prettyPrint(): String {
        val prettyReturnType = if (returnType == null) "" else ": $returnType"
        val prettyParameters: String =
            if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
        val ext = if (extensionOf == null) "" else "$extensionOf."
        val visibility = if (visibility == Visibility.PUBLIC) "" else "${visibility.toString().lowercase()} "
        return "${visibility}fun $ext$name($prettyParameters)$prettyReturnType"
    }
}

enum class Visibility {
    PUBLIC, PRIVATE, INTERNAL
}

enum class ObjectType {
    CLASS, OBJECT, DATA_CLASS, INTERFACE, ENUM
}

data class ClassSignature(
    val name: String,
    val parameters: List<Parameter> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val inheritedFrom: List<String> = listOf(),
    val visibility: Visibility = Visibility.PUBLIC,
    val type: ObjectType = ObjectType.CLASS,
) : PrettyPrint {
    override fun prettyPrint(): String {
        val inherited = if (inheritedFrom.isEmpty()) "" else ": " + inheritedFrom.joinToString(", ")
        val prettyFunctions: List<String> = functions.map { "    ${it.prettyPrint()}" }
        val prettyParameters: String =
            if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
        return "class $name($prettyParameters) $inherited {\n${prettyFunctions.joinToString("\n")}\n}"
    }
}
