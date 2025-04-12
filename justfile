# print this help
help:
  @just --list

# build the application, run all the tests
build:
    gradle clean check

# opens the coverage report in the default browser
coverage-report:
    open build/reports/jacoco/test/html/index.html

# run the application
run *args:
    @gradle run --args="{{args}}"
