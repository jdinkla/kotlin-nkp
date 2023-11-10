package net.dinkla.kpnk

data class File(
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
)

@JvmInline
value class FullyQualifiedName(private val name: String) {
    override fun toString(): String = name
}

data class Import(val name: FullyQualifiedName) {
    override fun toString(): String = "import $name"
}

data class Parameter(val name: String, val type: String) {
    override fun toString(): String = "$name: $type"
}

data class FunctionSignature(val name: String, val returnType: String, val parameters: List<Parameter>) {
    override fun toString(): String {
        return "fun $name(${prettyParameters}): $returnType"
    }

    private val prettyParameters: String
        get() = if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.toString() }
}

data class ObjectSignature(val name: String)

data class ClassSignature(val name: String, val functions: List<FunctionSignature>)
