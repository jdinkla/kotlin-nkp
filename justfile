TEMP_MODEL := "generated/test-model.json"
PREFIX := "generated/test-"

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

# run all the tasks for the given REPOSITORY
all-tasks REPOSITORY:
    just run parse {{REPOSITORY}} {{TEMP_MODEL}}

    just run inheritance-report {{TEMP_MODEL}} > {{PREFIX}}inheritance-report.json

    just run outlier-report {{TEMP_MODEL}} > {{PREFIX}}outlier-report.json
    just run search-report {{TEMP_MODEL}} Defined > {{PREFIX}}search-report.json
    just run packages-report {{TEMP_MODEL}} > {{PREFIX}}packages-report.json

    just run class-statistics {{TEMP_MODEL}} > {{PREFIX}}class-statistics.json
    just run file-statistics {{TEMP_MODEL}} > {{PREFIX}}file-statistics.json
    just run package-statistics {{TEMP_MODEL}} > {{PREFIX}}package-statistics.json

    just run mermaid-class-diagram {{TEMP_MODEL}} {{PREFIX}}mermaid-class-diagram.mermaid
    just run mermaid-class-diagram {{TEMP_MODEL}} {{PREFIX}}mermaid-class-diagram.html
    just run mermaid-import-diagram {{TEMP_MODEL}} {{PREFIX}}mermaid-import-diagram.mermaid
    just run mermaid-import-diagram {{TEMP_MODEL}} {{PREFIX}}mermaid-import-diagram.html

all-tests:
    jq empty {{PREFIX}}inheritance-report.json
    jq empty {{PREFIX}}outlier-report.json
    jq empty {{PREFIX}}search-report.json
    jq empty {{PREFIX}}packages-report.json
    jq empty {{PREFIX}}class-statistics.json
    jq empty {{PREFIX}}file-statistics.json
    jq empty {{PREFIX}}package-statistics.json
