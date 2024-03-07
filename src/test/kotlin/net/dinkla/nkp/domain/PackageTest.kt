package net.dinkla.nkp.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.examplePackage

class PackageTest : StringSpec({
    "should aggregate declarations" {
        val numberOfDeclarations = examplePackage.files.map { it.declarations.size }.sum()
        examplePackage.declarations.size shouldBe numberOfDeclarations
    }

    "should aggregate distinct imports" {
        examplePackage.imports().size shouldBe 3
    }
})
