package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import net.dinkla.nkp.EXAMPLE_MODEL_FILE
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadJson

class SearchTest :
    StringSpec({
        "should return search results for a valid class name" {
            val project = EXAMPLE_MODEL_FILE.loadJson<Project>()
            val search = project.search("Declaration")

            search.classes shouldHaveAtLeastSize 1
            search.superClasses shouldNotBe null
            search.subClasses shouldNotBe null
        }

        "should return an empty result for a non-existent class name" {
            val project = EXAMPLE_MODEL_FILE.loadJson<Project>()
            val search = project.search("NonExistentClass")

            search.classes shouldHaveSize 0
            search.superClasses shouldHaveSize 0
            search.subClasses shouldHaveSize 0
        }
    })
