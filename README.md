# kotlin-nkp

Kotlin-NKP is a project for static analysis of Kotlin programs (nkp is for aNalysis of Kotlin Programs). 

It can generate some metrics and create diagrams.

## Features

You can run the program with `bin/nkp.sh` or with `just run`.

```shell
$ bin/nkp.sh -h 
Usage: nkp [<options>] <command> [<args>]...

Options:
  -h, --help  Show this message and exit

Commands:
  parse                     Parse a source directory and generate a model file.
  class-statistics          Class statistics
  file-statistics           File statistics and imports report
  mermaid-class-diagram     Mermaid class diagram
  mermaid-coupling-diagram  Generate a Mermaid coupling diagram from code analysis
  mermaid-import-diagram    Mermaid import diagram
  package-coupling          Generate package coupling metrics
  package-statistics        Package statistics
  packages                  Packages report
  search                    Search for a class by name
```

One example is the import flow diagram:

![import diagram](docs/import-diagram.webp)

## Installation

### Prerequisites

- Java 17+ (for building dependencies)
- Gradle (included via wrapper)
- [just](https://github.com/casey/just) (optional, for convenience commands)
- [ktlint](https://github.com/pinterest/ktlint) (optional, for code formatting)

### Build Dependencies

This project uses custom libraries to parse Kotlin source code. You need to build these first:

- https://github.com/Kotlin/grammar-tools
- https://github.com/Kotlin/kotlin-spec

Run the installation script:

```sh
$ bin/install-libs.sh
```

**Note:** The installation script requires Java 17. The project itself targets JVM 21.

### Optional Tools

- [mermaid-cli](https://github.com/mermaid-js/mermaid-cli) - useful for converting Mermaid diagrams to SVG/HTML

## Usage

The first step is to parse the files in a directory to a JSON file.

```sh
$ bin/nkp.sh parse /repositories/ray-tracer-challenge/src/main/kotlin generated/model.json
```

Use this JSON file in the analysis steps as input.

### Examples

Generate a Mermaid class diagram:
```sh
$ bin/nkp.sh mermaid-class-diagram generated/model.json > generated/class-diagram.mermaid
```

Generate statistics:
```sh
$ bin/nkp.sh class-statistics generated/model.json > generated/class-statistics.json
$ bin/nkp.sh file-statistics generated/model.json > generated/file-statistics.json
$ bin/nkp.sh file-statistics --include-private-declarations generated/model.json > generated/file-statistics-full.json
$ bin/nkp.sh package-statistics generated/model.json > generated/package-statistics.json
```

Generate diagrams:
```sh
$ bin/nkp.sh mermaid-import-diagram generated/model.json > generated/import-diagram.mermaid
$ bin/nkp.sh mermaid-import-diagram --include-all-libraries generated/model.json > generated/import-diagram-all.mermaid
$ bin/nkp.sh mermaid-coupling-diagram generated/model.json > generated/coupling-diagram.mermaid
$ bin/nkp.sh mermaid-coupling-diagram --include-all-libraries generated/model.json > generated/coupling-diagram-all.mermaid
```

Search for classes:
```sh
$ bin/nkp.sh search generated/model.json MyClass
```

List packages:
```sh
$ bin/nkp.sh packages generated/model.json > generated/packages.json
```

## Building and Developing

### Build the Project

```sh
$ ./gradlew build
```

### Run Tests

```sh
$ ./gradlew test
```

### View Test Coverage

```sh
$ ./gradlew jacocoTestReport
$ open build/reports/jacoco/test/html/index.html
```

### Dependencies

The project uses [refreshVersions](https://splitties.github.io/refreshVersions/) for dependency management.

To update dependency versions:

```sh
$ ./gradlew refreshVersions
```

(c) 2023 - 2025 Jörn Dinkla https://www.dinkla.net
