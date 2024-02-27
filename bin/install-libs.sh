#!/usr/bin/env bash

echo "This does work with Java 17 and did not work with Java 21. Make sure you have Java 17 installed."
java -version

mkdir -p dependencies
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