package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Declaration
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.Import
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.domain.VisibilityModifier

enum class DeclarationFilter(
    val filter: (Declaration) -> Boolean,
) {
    EXCLUDE_PRIVATE_DECLARATIONS({ it.visibilityModifier != VisibilityModifier.PRIVATE }),
    INCLUDE_ALL({ true }),
    ;

    companion object {
        fun select(flag: Boolean) = if (flag) INCLUDE_ALL else EXCLUDE_PRIVATE_DECLARATIONS
    }
}

enum class ImportFilter(
    val filter: (KotlinFile, Import) -> Boolean,
) {
    EXCLUDE_IMPORTS_FROM_OTHER_PACKAGES({ file, import -> !import.name.packageName.isOtherPackage(file.packageName) }),
    INCLUDE_ALL({ _, _ -> true }),
    ;

    companion object {
        fun select(flag: Boolean) = if (flag) INCLUDE_ALL else EXCLUDE_IMPORTS_FROM_OTHER_PACKAGES
    }
}

fun fileImports(
    project: Project,
    declarationFilter: DeclarationFilter = DeclarationFilter.INCLUDE_ALL,
    importFilter: ImportFilter = ImportFilter.INCLUDE_ALL,
): List<FileImports> =
    project.files.map { file ->
        fileImports(file, declarationFilter, importFilter)
    }

private fun fileImports(
    file: KotlinFile,
    declarationFilter: DeclarationFilter,
    importFilter: ImportFilter,
): FileImports {
    val pname = file.packageName.toString()
    val declarations =
        file.declarations
            .filter { declarationFilter.filter(it) }
            .map { GeneralDeclaration("$pname.${it.name}", it.visibilityModifier) }
    val imports =
        file.imports.filter { importFilter.filter(file, it) }
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
