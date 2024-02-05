package net.dinkla.kpnk.analysis

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package
import java.io.File

fun overview(
    files: Files,
    file: File
) {
    val packages = files.packages()
    val imports = overviewStats(packages)
    val string = Json.encodeToString(imports)
    file.writeText(string)
}

@Serializable
private data class OverviewStats(
    val name: String,
    val files: Int,
    val functions: Int,
    val properties: Int,
    val classes: Int,
    val typeAliases: Int,
) {
    override fun toString(): String = "$name, $files, $classes, $functions, $properties, $typeAliases"

    companion object {
        fun from(p: Package): OverviewStats {
            val name = p.packageName.name
            val files = p.files.size
            val functions = p.functions.size
            val properties = p.properties.size
            val classes = p.classes.size
            val typeAliases = p.typeAliases.size
            return OverviewStats(name, files, functions, properties, classes, typeAliases)
        }
    }
}

private fun overviewStats(packages: List<Package>): List<OverviewStats> = packages.map { OverviewStats.from(it) }
