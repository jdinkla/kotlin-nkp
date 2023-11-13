package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.elements.FullyQualifiedName
import net.dinkla.kpnk.elements.Import
import net.dinkla.kpnk.elements.TypeAlias
import net.dinkla.kpnk.fromText

class ExtractTest : StringSpec({
    "extract should return all information" {
        val file = extract(tree)
        file.packageName shouldBe FullyQualifiedName("example")
        file.imports shouldContainExactly expectedImports
        file.functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3, function4, function5)
        file.classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
    }

    "extractPackageName should return the fully qualified package name" {
        val packageName = extractPackageName(fromText("package my.example.test"))
        packageName shouldBe FullyQualifiedName("my.example.test")
    }

    "extractPackageName should return the simple package name" {
        val packageName = extractPackageName(fromText("package example"))
        packageName shouldBe FullyQualifiedName("example")
    }

    "extractImports should return all imports" {
        val imports = extractImports(fromText("package example; import a.b.c; import d.e.f"))
        imports shouldContainExactly listOf(Import(FullyQualifiedName("a.b.c")), Import(FullyQualifiedName("d.e.f")))
    }

    "extractTypeAlias should extract typealias" {
        val typeAliases = extractTypeAliases(fromText("typealias Dictionary = Map<String, String>"))
        typeAliases shouldContainExactly listOf(TypeAlias("Dictionary", "Map"))
    }
})
