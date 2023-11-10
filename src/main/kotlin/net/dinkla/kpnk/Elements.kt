package net.dinkla.kpnk

data class File(
    val imports: List<Import> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
)

@JvmInline
value class FullyQualifiedName(val name: String) {
    override fun toString(): String = name
}

data class Import(val fullyQualifiedName: FullyQualifiedName) {
    override fun toString(): String = "Import($fullyQualifiedName)"
}

data class Parameter(val name: String, val type: String)

data class FunctionSignature(val name: String, val returnType: String, val parameters: List<Parameter>)

data class ObjectSignature(val name: String)

data class ClassSignature(val name: String, val functions: List<FunctionSignature>)
