package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.AnalysedFile
import net.dinkla.kpnk.domain.Defined
import net.dinkla.kpnk.domain.FileName
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.PackageName
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

fun extract(
    fileName: FileName,
    tree: KotlinParseTree,
): AnalysedFile {
    val packageName = extractPackageName(tree)
    val imports = extractImports(tree)
    val declarations = extractDefinitions(tree)
    return AnalysedFile(fileName, packageName, imports, declarations)
}

internal fun extractPackageName(tree: KotlinParseTree): PackageName {
    val packageHeader = tree.children.find { it.name == "packageHeader" }
    return PackageName(
        if (packageHeader != null) {
            extractFullyQualifiedPackageName(packageHeader)
        } else {
            ""
        },
    )
}

private fun extractFullyQualifiedPackageName(tree: KotlinParseTree) =
    tree.children[1].children
        .filter { it.name == "simpleIdentifier" }
        .map { extractIdentifier(it) }
        .joinToString(".")

internal fun extractImports(tree: KotlinParseTree): List<Import> =
    tree.children.find { it.name == "importList" }?.let { importList ->
        importList.children.map { importHeader ->
            assert(importHeader.name == "importHeader")
            val fullyQualifiedImport =
                importHeader.children[1].children.joinToString("", transform = ::extractIdentifier)
            Import(ImportedElement(fullyQualifiedImport))
        }
    } ?: listOf()

internal fun extractDefinitions(tree: KotlinParseTree): List<Defined> {
    val result = mutableListOf<Defined>()
    for (declaration in getDeclarations(tree)) {
        when (declaration.name) {
            "classDeclaration" -> result += extractClass(declaration)
            "objectDeclaration" -> result += extractObject(declaration)
            "functionDeclaration" -> result += extractFunctionSignature(declaration)
            "typeAlias" -> result += extractTypeAlias(declaration)
            "propertyDeclaration" -> result += extractProperty(declaration)
        }
    }
    return result
}

internal fun getDeclarations(tree: KotlinParseTree): List<KotlinParseTree> =
    tree.children
        .filter { it.name == "topLevelObject" }
        .map { it.children[0].children[0] }
