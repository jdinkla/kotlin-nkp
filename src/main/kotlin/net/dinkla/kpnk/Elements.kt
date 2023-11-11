package net.dinkla.kpnk

@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name
}

sealed interface FileInfo {
    data class Parsed(
        val fileName: String,
        val elements: Elements,
    ) : FileInfo {
        fun basename(): String {
            val name = basename(fileName).replace(".kt", "")
            val fullName = elements.packageName.toString() + "." + name
            return fullName
        }
    }

    data class Error(val fileName: String, val message: String) : FileInfo
}

data class Elements(
    val packageName: FullyQualifiedName,
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val classes: List<ClassSignature> = listOf(),
) {
    override fun toString(): String = """
package $packageName
${imports.joinToString("\n")}
${functions.joinToString("\n")}
${classes.joinToString("\n")}
    """.trimIndent()
}

data class Import(val name: FullyQualifiedName) {
    override fun toString(): String = "import $name"
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

data class Parameter(val name: String, val type: String) {
    override fun toString(): String = "$name: $type"
}

data class FunctionSignature(val name: String, val returnType: String?, val parameters: List<Parameter>) {
    override fun toString(): String {
        return "fun $name($prettyParameters): $returnType"
    }

    private val prettyParameters: String
        get() = if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.toString() }
}

data class ObjectSignature(val name: String, val parameters: List<Parameter>, val functions: List<FunctionSignature>)

data class ClassSignature(val name: String, val parameters: List<Parameter>, val functions: List<FunctionSignature>) {
    override fun toString(): String {
        return "class $name($prettyParameters) {\n${prettyFunctions.joinToString("\n")}\n}"
    }

    private val prettyParameters: String
        get() = if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.toString() }

    private val prettyFunctions: List<String>
        get() = functions.map { "    $it" }
}
