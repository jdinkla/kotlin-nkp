package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import net.dinkla.nkp.EXAMPLE_MODEL_FILE
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class SearchTest :
    StringSpec({
        "should return search results for a valid class name" {
            val project = loadFromJsonFile<Project>(EXAMPLE_MODEL_FILE)
            val search = project.search("Declaration")

            search.classes shouldHaveAtLeastSize 1
            search.hierarchy shouldNotBe null
            search.implementers shouldNotBe null
        }

        "should return an empty result for a non-existent class name" {
            val project = loadFromJsonFile<Project>(EXAMPLE_MODEL_FILE)
            val search = project.search("NonExistentClass")

            search.classes shouldHaveSize 0
            search.hierarchy shouldHaveSize 0
            search.implementers shouldHaveSize 0
        }
    })
