# kotlin-nkp

Analyse Kotlin source code.

## Run

The command line syntax would be as follows if this would be a unix tool!

```sh
$ kpnk (directory|jsonfile) <command> [args of command]
```

But the arguments have to be separated by command and passed to gradle like the following example for Windows.

```sh
./gradlew run -Pargs="C:\directory\project\src\commonMain\kotlin,dependencies"
./gradlew run -Pargs="generated/infos.json,dependencies,--output,deps.json" 
```

Example for *nix.

```sh
./gradlew run -Pargs="../directory/project/src/"
```

## Build 

To build this application the following other dependencies are needed.
### Dependencies

```sh
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

### Upgrade dependencies

The project uses [refreshVersions](https://splitties.github.io/refreshVersions/)

```sh
$ gradle refreshVersions
```

(c) 2023 - 2024 Jörn Dinkla https://www.dinkla.net
