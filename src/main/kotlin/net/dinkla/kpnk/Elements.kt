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

data class FunctionSignature(val name: String, val returnType: String?, val parameters: List<Parameter>) : PrettyPrint {
    override fun prettyPrint(): String {
        return "fun $name($prettyParameters)$prettyReturnType"
    }

    private val prettyReturnType = if (returnType == null) "" else ": $returnType"

    private val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
}

data class ObjectSignature(val name: String, val parameters: List<Parameter>, val functions: List<FunctionSignature>)

data class ClassSignature(
    val name: String,
    val parameters: List<Parameter> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val inheritedFrom: List<String> = listOf(),
) : PrettyPrint {
    override fun prettyPrint(): String {
        val inherited = if (inheritedFrom.isEmpty()) "" else ": " + inheritedFrom.joinToString(", ")
        return "class $name($prettyParameters) $inherited {\n${prettyFunctions.joinToString("\n")}\n}"
    }

    private val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }

    private val prettyFunctions: List<String> = functions.map { "    ${it.prettyPrint()}" }
}
