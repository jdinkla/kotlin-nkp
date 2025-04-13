# print this help
help:
  @just --list

# build the application, run all the tests
build:
    gradle clean check

# ktlint
lint:
    @ktlint -F

# opens the coverage report in the default browser
coverage-report:
    open build/reports/jacoco/test/html/index.html

# create example model from this project
example-model:
    @gradle run --quiet --args="parse . src/test/resources/model.json"

# run the application
run *args:
    @gradle run --quiet --args="{{args}}"

