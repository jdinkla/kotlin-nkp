# kotlin-nkp

Analyse Kotlin source code.

## Usage

The command line syntax are as following:

```sh
$ bin/nkp.sh (directory|jsonfile) <command> [args of command]
```

Help is available with `-h` or `--help`.

```sh
$ bin/nkp.sh -h
```

It is advisable to first parse the source code into a json file.

```sh
$ bin/nkp.sh /repositories/ray-tracer-challenge/src/main/kotlin --save=generated/rtc.json
```

Use this JSON file in the following commands as input.

```sh
$ bin/nkp.sh generated/rtc.json --mermaid-class-diagram=generated/rtc.mermaid
```

## Build 

This project is using the following libraries to parse Kotlin source code:

- https://github.com/Kotlin/grammar-tools
- https://github.com/Kotlin/kotlin-spec

You have to build these libraries first locally. 

```sh
$ bin/install-libs.sh
```

## Dependencies

The project uses [refreshVersions](https://splitties.github.io/refreshVersions/)

```sh
$ gradle refreshVersions
```

(c) 2023 - 2024 Jörn Dinkla https://www.dinkla.net
