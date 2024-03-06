package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package
import net.dinkla.kpnk.domain.PackageName
import java.io.File

fun mermaidImportsFlowDiagram(
    files: Files,
    outputFile: File,
) {
    val packagesList = files.packages()
    val packagesTree =  toTree(packagesList)
    val content = generateDiagram(packagesTree, packagesList)
    save(outputFile, content)
}

class TreeNode<T>(val value: T, val children: MutableList<TreeNode<T>> = mutableListOf()) {
    fun addChild(node: TreeNode<T>) {
        children.add(node)
    }

    override fun toString(): String {
        return "TreeNode(value=$value, children=$children)"
    }
}

fun toTree(packages: List<Package>): TreeNode<Package> {
    val root = TreeNode(Package(PackageName(""), emptyList()))
    packages.forEach { pkg ->
        val parts = pkg.packageName.name.split(".")
        var current = root
        for (part in parts) {
            val found = current.children.find { it.value.packageName.name == part }
            if (found != null) {
                current = found
            } else {
                val newNode = TreeNode(Package(PackageName(part), emptyList()))
                current.addChild(newNode)
                current = newNode
            }
        }
        current.addChild(TreeNode(pkg))
    }
    return root
}

private fun generateDiagram(tree: TreeNode<Package>, packages: List<Package>) =
    buildString {
        appendLine("flowchart LR")
        generateDiagramRecursive(tree, this)
        val imports = packages.flatMap { pkg ->
            pkg.files.flatMap { file ->
                file.imports.map { imp ->
                    "  ${file.packageName.name} --> ${imp.name.packageName.name}"
                }
            }
        }
        val distinctImports = imports.sortedBy { it }.distinct()
        distinctImports.forEach { appendLine(it) }
    }

fun generateDiagramRecursive(
    tree: TreeNode<Package>,
    stringBuilder: StringBuilder,
    indent: Int = 0,
) {
    if (tree.value.packageName.name.isEmpty()) {
        tree.children.forEach {
            generateDiagramRecursive(it, stringBuilder, indent)
        }
        return
    }
    val spaces = " ".repeat(indent * 2)
    stringBuilder.appendLine("${spaces}subgraph ${tree.value.packageName.name}")
    tree.children.forEach { child ->
        if (child.children.isEmpty()) {
            val id = child.value.packageName.name
            stringBuilder.appendLine("$spaces  $id")
//            child.value.files.forEach { file ->
//                val id = "${child.value.packageName.name}.${file.fileName.basename.replace(".kt", "")}"
//                val id = child.value.packageName.name
//                stringBuilder.appendLine("$spaces  $id[\"${file.fileName.basename}\"]")
//            }
        } else {
            generateDiagramRecursive(child, stringBuilder, indent + 1)
        }
    }
    stringBuilder.appendLine("${spaces}end")
}
