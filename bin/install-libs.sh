#!/bin/sh

mkdir dependencies
cd dependencies

git clone https://github.com/Kotlin/kotlin-spec.git
cd kotlin-spec
./gradlew :grammar:publishToMavenLocal
cd ..

git clone https://github.com/Kotlin/grammar-tools
cd grammar-tools
./gradlew publishToMavenLocal
cd ..

cd ..