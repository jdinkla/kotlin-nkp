package net.dinkla.nkp.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.class1
import net.dinkla.nkp.class2
import net.dinkla.nkp.class3
import net.dinkla.nkp.class4
import net.dinkla.nkp.class5
import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.Import
import net.dinkla.nkp.domain.ImportedElement
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Type
import net.dinkla.nkp.domain.TypeAlias
import net.dinkla.nkp.enum1
import net.dinkla.nkp.enum2
import net.dinkla.nkp.expectedImports
import net.dinkla.nkp.function1
import net.dinkla.nkp.function2
import net.dinkla.nkp.function3
import net.dinkla.nkp.function4
import net.dinkla.nkp.function5
import net.dinkla.nkp.property1
import net.dinkla.nkp.property2
import net.dinkla.nkp.property3
import net.dinkla.nkp.tree
import net.dinkla.nkp.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class TopLevelTest :
    StringSpec({
        "extract should return all information" {
            val file = extract(FilePath(""), tree)
            file.packageName shouldBe PackageName("example")
            file.imports shouldContainExactly expectedImports
            file.functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3, function4, function5)
            file.classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
            file.properties shouldContainExactlyInAnyOrder listOf(property1, property2, property3)
        }

        "extractPackageName should return the fully qualified package name" {
            val packageName = extractPackageName(fromText("package my.example.test"))
            packageName shouldBe PackageName("my.example.test")
        }

        "extractPackageName should return the simple package name" {
            val packageName = extractPackageName(fromText("package example"))
            packageName shouldBe PackageName("example")
        }

        "extractImports should return all imports" {
            val imports = extractImports(fromText("package example; import a.b.c; import d.e.f"))
            imports shouldContainExactly listOf(Import(ImportedElement("a.b.c")), Import(ImportedElement("d.e.f")))
        }

        "extractTypeAlias should extract typealias" {
            val typeAliases = extractTypeAliases(fromText("typealias Dictionary = Map<String, String>"))
            typeAliases shouldContainExactly listOf(TypeAlias("Dictionary", Type("Map<String,String>")))
        }

        "extractTypeAlias should extract generic typealias" {
            val typeAliases = extractTypeAliases(fromText("typealias Dictionary<K> = Map<K, String>"))
            typeAliases shouldContainExactly listOf(TypeAlias("Dictionary", Type("Map<K,String>")))
        }
    })

internal fun extractTypeAliases(tree: KotlinParseTree): List<TypeAlias> =
    getDeclarations(tree)
        .filter { it.name == "typeAlias" }
        .map { extractTypeAlias(it) }
