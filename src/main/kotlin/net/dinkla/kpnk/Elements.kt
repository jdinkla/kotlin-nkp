package net.dinkla.kpnk

@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name
}

data class File(
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
}

data class Parameter(val name: String, val type: String) {
    override fun toString(): String = "$name: $type"
}

data class FunctionSignature(val name: String, val returnType: String, val parameters: List<Parameter>) {
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
