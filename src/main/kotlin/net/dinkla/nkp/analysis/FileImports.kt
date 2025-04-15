package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.Import
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.domain.VisibilityModifier

fun allFileImports(project: Project): List<FileImports> =
    project.files.map { file ->
        fileImports(file)
    }

private fun fileImports(file: KotlinFile): FileImports {
    val pname = file.packageName.toString()
    val declarations =
        file.declarations.map {
            GeneralDeclaration("$pname.${it.name}", it.visibilityModifier)
        }
    return FileImports(
        file.filePath,
        file.imports,
        declarations,
        Coupling(declarations.size, file.imports.size),
    )
}

fun filteredFileImports(project: Project): List<FileImports> =
    project.files.map { file ->
        filteredFileImports(file)
    }

private fun filteredFileImports(file: KotlinFile): FileImports {
    val pname = file.packageName.toString()
    val declarations =
        file.declarations
            .filter { it.visibilityModifier != VisibilityModifier.PRIVATE }
            .map { GeneralDeclaration("$pname.${it.name}", it.visibilityModifier) }
    val imports =
        file.imports.filter {
            !it.name.packageName.isOtherPackage(file.packageName)
        }
    return FileImports(
        file.filePath,
        imports,
        declarations,
        Coupling(declarations.size, imports.size),
    )
}

@Serializable
data class GeneralDeclaration(
    val name: String,
    val visibilityModifier: VisibilityModifier?,
)

@Serializable
data class FileImports(
    val filePath: FilePath,
    val imports: List<Import>,
    val declarations: List<GeneralDeclaration>,
    val coupling: Coupling,
)

@Serializable
data class Coupling(
    val afferentCoupling: Int,
    val efferentCoupling: Int,
    val instability: Double,
) {
    constructor(afferentCoupling: Int, efferentCoupling: Int) : this(
        afferentCoupling,
        efferentCoupling,
        if (efferentCoupling + afferentCoupling > 0) {
            efferentCoupling.toDouble() / (efferentCoupling + afferentCoupling)
        } else {
            0.0
        },
    )
}
