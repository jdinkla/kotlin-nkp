package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.Files

fun debug(files: Files) {
    println("*************** DEBUG ***************")
    val ds = AnalyzedPackage.from(files)
    println(ds)
}
