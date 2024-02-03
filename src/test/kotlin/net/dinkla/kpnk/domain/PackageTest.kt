package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.examplePackage

class PackageTest : StringSpec({
    "should aggregate declarations" {
        val numberOfDeclarations = examplePackage.files.map { it.declarations.size }.sum()
        examplePackage.declarations.size shouldBe numberOfDeclarations
    }

    "should aggregate imports" {
        val numberOfImports = examplePackage.files.map { it.imports.size }.sum()
        examplePackage.imports().size shouldBe numberOfImports
    }
})
