package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.Coupling
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.Import
import net.dinkla.nkp.domain.ImportedElement
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.domain.VisibilityModifier
import net.dinkla.nkp.exampleProject
import net.dinkla.nkp.kotlinFile

class FileImportsTest :
    StringSpec({
        "should return the imports for every package" {
            val result = fileImports(exampleProject).sortedBy { it.filePath }
            result shouldHaveSize 3
            result[0].filePath shouldBe kotlinFile.filePath
            result[0].coupling shouldBe Coupling(5, 2, 2.0 / (5 + 2))
        }

        "should return the imports for filtered packages" {
            val filePath = FilePath("some")
            val theImport = Import(ImportedElement("my.pack.Age"))
            val declaration1 = ClassSignature("A", visibilityModifier = VisibilityModifier.INTERNAL)
            val declaration2 = ClassSignature("B", visibilityModifier = VisibilityModifier.PRIVATE)
            val declaration3 = ClassSignature("C")
            val kotlinFile =
                KotlinFile(
                    filePath = filePath,
                    packageName = PackageName("my.pack"),
                    imports =
                        listOf(
                            Import(ImportedElement("java.lang.something")),
                            theImport,
                        ),
                    declarations =
                        listOf(
                            declaration1,
                            declaration2,
                            declaration3,
                        ),
                )
            val expected =
                FileImports(
                    filePath = filePath,
                    imports = listOf(theImport),
                    declarations =
                        listOf(
                            GeneralDeclaration("my.pack.A", VisibilityModifier.INTERNAL),
                            GeneralDeclaration("my.pack.C", null),
                        ),
                    coupling = Coupling(2, 1, 1.0 / (2 + 1)),
                )
            val project = Project("/base", listOf(kotlinFile))
            val result =
                fileImports(
                    project,
                    DeclarationFilter.EXCLUDE_PRIVATE_DECLARATIONS,
                    ImportFilter.EXCLUDE_IMPORTS_FROM_OTHER_PACKAGES,
                ).sortedBy {
                    it.filePath
                }
            result[0].imports shouldBe expected.imports
            result[0].declarations shouldBe expected.declarations
            result[0].coupling shouldBe expected.coupling
            result shouldContainExactly listOf(expected)
        }
    })
