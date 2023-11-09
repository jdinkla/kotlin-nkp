# kotlin-nkp

Analyse Kotlin source code.

WIP

This project is using the following libraries to parse Kotlin source code:

https://github.com/Kotlin/grammar-tools
https://github.com/Kotlin/kotlin-spec

```sh
$ git clone https://github.com/Kotlin/kotlin-spec.git
$ cd kotlin-spec
$ ./gradlew :grammar:publishToMavenLocal
$ cd ..

$ git clone https://github.com/Kotlin/grammar-tools
$ cd grammar-tools
$ ./gradlew publishToMavenLocal
$ cd ..

```

(c) 2023 Jörn Dinkla https://www.dinkla.net
