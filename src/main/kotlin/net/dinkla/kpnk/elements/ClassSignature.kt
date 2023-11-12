package net.dinkla.kpnk.elements

data class ClassSignature(
    val name: String,
    val parameters: List<Parameter> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val inheritedFrom: List<String> = listOf(),
    val visibilityModifier: VisibilityModifier? = null,
    val elementType: Type = Type.CLASS,
    val classModifier: ClassModifier? = null,
    val inheritanceModifier: InheritanceModifier? = null,
) : PrettyPrint {
    override fun prettyPrint(): String {
        val inherited = if (inheritedFrom.isEmpty()) "" else " : " + inheritedFrom.joinToString(", ")
        val prettyParameters: String =
            if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
        val visibility = visibilityModifier.prettyPrint()
        val joined = functions.map { "    ${it.prettyPrint()}" }.joinToString("\n")
        val classMod = when (classModifier) {
            null -> ""
            ClassModifier.DATA -> " data "
            ClassModifier.ENUM -> " enum "
        }
        return "${visibility}${classMod}${elementType.text} $name($prettyParameters)$inherited {\n$joined\n}"
    }
}
