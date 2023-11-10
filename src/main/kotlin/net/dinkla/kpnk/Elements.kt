package net.dinkla.kpnk

data class Import(val fullyQualifiedName: String)

data class Parameter(val name: String, val type: String)

data class FunctionSignature(val name: String, val returnType: String, val parameters: List<Parameter>)

data class ObjectSignature(val name: String)

data class ClassSignature(val name: String, val functions: List<FunctionSignature>)

