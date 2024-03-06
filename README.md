# kotlin-nkp

Kotlin-NKP is a project for static analysis of Kotlin programs (nkp is for aNalysis of Kotlin Programs). 

It can generate some metrics and create diagrams.

**WIP: This project is work in progress. It is at the start of it's life cycle**

## Features

```shell
$ bin/nkp.sh -h
...
Usage: main [<options>] <source>

Options:
  --class-statistics=<path>    statistics on class level
  --debug                      debug
  --file-statistics=<path>     statistics for all files
  --mermaid-class-diagram=<path>
                               Generate a mermaid class diagram (.mermaid or
                               .html)
  --mermaid-imports-flow-diagram=<path>
                               Generate mermaid flow diagram for imports
                               (.mermaid or .html)
  --packages=<path>            exports all information organized by packages
  --package-statistics=<path>  analysis for all packages
  --save=<path>                save parsed source code as json
  --inheritance=<path>
  --outliers=<path>
  --search=<text>
  -h, --help                   Show this message and exit
```

## Usage

The command line syntax are as following:

```sh
$ bin/nkp.sh (directory|jsonfile) <command> [args of command]
```

Help is available with `-h` or `--help`.

```sh
$ bin/nkp.sh -h
```

## Typical workflow

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
