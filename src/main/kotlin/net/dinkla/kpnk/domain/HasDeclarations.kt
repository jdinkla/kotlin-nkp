package net.dinkla.kpnk.domain

interface HasDeclarations {
    val declarations: List<Defined>

    val functions: List<FunctionSignature>
        get() = declarations.filterIsInstance<FunctionSignature>()

    val properties: List<Property>
        get() = declarations.filterIsInstance<Property>()

    val classes: List<ClassSignature>
        get() = declarations.filterIsInstance<ClassSignature>()

    val typeAliases: List<TypeAlias>
        get() = declarations.filterIsInstance<TypeAlias>()
}
