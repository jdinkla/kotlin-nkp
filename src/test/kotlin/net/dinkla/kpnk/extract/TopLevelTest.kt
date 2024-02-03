package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.class1
import net.dinkla.kpnk.class2
import net.dinkla.kpnk.class3
import net.dinkla.kpnk.class4
import net.dinkla.kpnk.class5
import net.dinkla.kpnk.domain.FileName
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.PackageName
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.TypeAlias
import net.dinkla.kpnk.enum1
import net.dinkla.kpnk.enum2
import net.dinkla.kpnk.expectedImports
import net.dinkla.kpnk.function1
import net.dinkla.kpnk.function2
import net.dinkla.kpnk.function3
import net.dinkla.kpnk.function4
import net.dinkla.kpnk.function5
import net.dinkla.kpnk.property1
import net.dinkla.kpnk.property2
import net.dinkla.kpnk.property3
import net.dinkla.kpnk.tree
import net.dinkla.kpnk.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class TopLevelTest : StringSpec({
    "extract should return all information" {
        val file = extract(FileName(""), tree)
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
