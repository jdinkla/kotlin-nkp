package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.examplePackage

class PackageTest : StringSpec({
    "should aggregate declarations" {
        val numberOfDeclarations = examplePackage.files.map { it.declarations.size }.sum()
        examplePackage.declarations.size shouldBe numberOfDeclarations
    }

    "should aggregate distinct imports" {
        val numberOfImports = examplePackage.files.map { it.imports }.toSet().sumOf { it.size }
        examplePackage.imports().size shouldBe numberOfImports
    }
})
