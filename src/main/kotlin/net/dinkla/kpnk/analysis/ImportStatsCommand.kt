package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package
import java.io.File

object ImportStatsCommand  {
    fun execute(
        files: Files,
        file: File
    ) {
        val packages = files.packages()
        val imports = imports(packages)
        val string = Json.encodeToString(imports)
        file.writeText(string)
    }
}

@Serializable
private data class ImportStats(
    val name: String,
    val total: Int,
    val fromSubPackage: Int,
    val fromSuperPackage: Int,
    val fromSidePackage: Int,
    val fromOtherPackage: Int,
) {
    override fun toString(): String
        = "$name, $total, $fromSubPackage, $fromSuperPackage, $fromSidePackage, $fromOtherPackage"

    companion object {
        fun from(p: Package): ImportStats {
            val packages = p.imports().map { it.name.packageName }.distinct()
            return ImportStats(
                p.packageName.name,
                p.imports().size,
                packages.count { it.isSubPackageOf(p.packageName) },
                packages.count { it.isSuperPackage(p.packageName) },
                packages.count { it.isSidePackage(p.packageName) },
                packages.count { it.isOtherPackage(p.packageName) },
            )
        }
    }
}

private fun imports(packages: List<Package>): List<ImportStats> = packages.map { ImportStats.from(it) }
