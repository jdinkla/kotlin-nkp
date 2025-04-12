# print this help
help:
  @just --list

# build the application, run all the tests
build:
    gradle clean check

coverage-report:
    open build/reports/jacoco/test/html/index.html
