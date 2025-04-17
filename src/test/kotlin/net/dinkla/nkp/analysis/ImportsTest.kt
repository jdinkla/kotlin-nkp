package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.kotlinlang.PackageName
import net.dinkla.nkp.exampleProject

class ImportsTest :
    StringSpec({
        "should return the imports for every package" {
            val result = allImports(exampleProject)
            result shouldHaveSize 1
            result[0].packageName shouldBe PackageName("net.dinkla.nkp")
            result[0].imports shouldContainExactly
                setOf(PackageName("java.lang.Boolean"), PackageName("net.dinkla.nkp"))
        }

        "should return the imports for every package but not external once" {
            val result = filteredImports(exampleProject)
            result shouldHaveSize 1
            result[0].packageName shouldBe PackageName("net.dinkla.nkp")
            result[0].imports shouldContainExactly
                setOf(PackageName("net.dinkla.nkp"))
        }
    })
