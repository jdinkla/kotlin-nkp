package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package

fun debug(files: Files) {
    println("*************** DEBUG ***************")
    val ds = Dependencies.from(files)
    println("ds: " + ds.dependencies.map { Pair(it.packageName, it.importedElements.size) })

    val packages = files.packages()
    println("ps: " +     packages.map { Pair(it.packageName, it.imports().size) })

    val pckg = "net.dinkla.kpnk.domain"

    // Package -> Imports
    val ds1: Dependency = ds.dependencies.find { it.packageName.name == pckg }!!
    println(ds1.importedElements.sortedBy { it.packageName.name }.map {it})

    // Package -> Files
    val ps1: Package = packages.find { it.packageName.name == pckg }!!
    println(ps1.imports().sortedBy { it.name.name }.map { it.name.name})

}