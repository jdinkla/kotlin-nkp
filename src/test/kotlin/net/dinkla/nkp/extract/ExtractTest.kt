package net.dinkla.nkp.extract

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.kotlinlang.Type
import net.dinkla.nkp.domain.kotlinlang.TypeAlias
import net.dinkla.nkp.utilities.fromText

class ExtractTest :
    StringSpec({
        "extractSimpleIdentifier should return identifier when simpleIdentifier is found" {
            // Given
            val tree = fromText("fun myFunction() = 1")
            val functionTree = getDeclarations(tree).first { it.name == "functionDeclaration" }
            // When
            val identifier = extractSimpleIdentifier(functionTree)
            // Then
            identifier shouldBe "myFunction"
        }

        "extractSimpleIdentifier should return null when simpleIdentifier is not found" {
            // Given
            val tree = fromText("typealias MyAlias = Int")
            val typeAliasTree = getDeclarations(tree).first { it.name == "typeAlias" }
            val typeNode = typeAliasTree.children.first { it.name == "type" }
            // When
            val identifier = extractSimpleIdentifier(typeNode)
            // Then
            identifier shouldBe null
        }

        "extractIdentifier should extract identifier from simpleIdentifier node" {
            // Given
            val tree = fromText("fun test() = 1")
            val functionTree = getDeclarations(tree).first { it.name == "functionDeclaration" }
            val simpleIdentifierNode = functionTree.children.find { it.name == "simpleIdentifier" }!!
            // When
            val identifier = extractIdentifier(simpleIdentifierNode)
            // Then
            identifier shouldBe "test"
        }

        "extractIdentifier should return dot for DOT node" {
            // Given
            val tree = fromText("import a.b.c")
            val importList = tree.children.find { it.name == "importList" }!!
            val importHeader = importList.children.first { it.name == "importHeader" }
            val dotNode = importHeader.children[1].children.find { it.name == "DOT" }!!
            // When
            val result = extractIdentifier(dotNode)
            // Then
            result shouldBe "."
        }

        "extractIdentifier should throw IllegalArgumentException for unknown node type" {
            // Given
            val tree = fromText("fun test() = 1")
            val functionTree = getDeclarations(tree).first { it.name == "functionDeclaration" }
            val unknownNode = functionTree.children.find { it.name == "functionValueParameters" }!!
            // When
            val exception =
                shouldThrow<IllegalArgumentException> {
                    extractIdentifier(unknownNode)
                }
            // Then
            exception.message shouldBe "Unknown child 'functionValueParameters' in '${unknownNode.toString()
                .replace(" ", "_").replace("[^a-zA-Z0-9_-]".toRegex(), "")}'"
        }

        "extractTypeAlias should extract simple typealias" {
            // Given
            val tree = fromText("typealias Dictionary = Map<String, String>")
            val typeAliasTree = getDeclarations(tree).first { it.name == "typeAlias" }
            // When
            val typeAlias = extractTypeAlias(typeAliasTree)
            // Then
            typeAlias shouldBe TypeAlias("Dictionary", Type("Map<String,String>"))
        }

        "extractTypeAlias should extract generic typealias" {
            // Given
            val tree = fromText("typealias Dictionary<K> = Map<K, String>")
            val typeAliasTree = getDeclarations(tree).first { it.name == "typeAlias" }
            // When
            val typeAlias = extractTypeAlias(typeAliasTree)
            // Then
            typeAlias shouldBe TypeAlias("Dictionary", Type("Map<K,String>"))
        }

        "extractTypeAlias should extract typealias with simple type" {
            // Given
            val tree = fromText("typealias MyInt = Int")
            val typeAliasTree = getDeclarations(tree).first { it.name == "typeAlias" }
            // When
            val typeAlias = extractTypeAlias(typeAliasTree)
            // Then
            typeAlias shouldBe TypeAlias("MyInt", Type("Int"))
        }

        "extractTypeAlias should extract typealias with nullable type" {
            // Given
            val tree = fromText("typealias NullableString = String?")
            val typeAliasTree = getDeclarations(tree).first { it.name == "typeAlias" }
            // When
            val typeAlias = extractTypeAlias(typeAliasTree)
            // Then
            typeAlias shouldBe TypeAlias("NullableString", Type("String?"))
        }
    })
