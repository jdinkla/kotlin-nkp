prefix := ""
generated := "generated"
model_file := generated / prefix + "model.json"

gradle := "./gradlew"

# print this help
help:
  @just --list


# build the application, run all the tests
build:
    @{{gradle}} check

# clean up
clean:
    @{{gradle}} clean

# ktlint
lint:
    @ktlint -F src/

# opens the coverage report in the default browser
coverage-report:
    open build/reports/jacoco/test/html/index.html

# create example model from this project
example-model:
    @{{gradle}} run --quiet --args="parse . src/test/resources/model.json"

# run the application
run *args:
    @{{gradle}} run --quiet --args="{{args}}"

# run all the tasks for the given REPOSITORY
all-tasks REPOSITORY:
    just run parse {{REPOSITORY}} {{model_file}}

    just run class-statistics {{model_file}} > {{generated}}/{{prefix}}class-statistics.json

    just run file-statistics --include-private-declarations {{model_file}} > {{generated}}/{{prefix}}file-statistics-private.json

    just run package-coupling {{model_file}} > {{generated}}/{{prefix}}package-coupling.json
    just run packages-report {{model_file}} > {{generated}}/{{prefix}}packages-report.json
    just run packages-statistics {{model_file}} > {{generated}}/{{prefix}}packages-statistics.json

    just run search {{model_file}} Declaration > {{generated}}/{{prefix}}search.json

    just run mermaid-class-diagram {{model_file}} > {{generated}}/{{prefix}}mermaid-class-diagram.mermaid
    just run mermaid-import-diagram {{model_file}} >  {{generated}}/{{prefix}}mermaid-import-diagram.mermaid
    just run mermaid-import-diagram {{model_file}} --include-all-libraries > {{generated}}/{{prefix}}mermaid-import-all-diagram.mermaid
    just run mermaid-coupling-diagram {{model_file}} > {{generated}}/{{prefix}}mermaid-coupling-diagram.mermaid
    just run mermaid-coupling-diagram {{model_file}} --include-all-libraries > {{generated}}/{{prefix}}mermaid-coupling-all-diagram.mermaid

# run all-task for this repository
all-tasks-self:
    just all-tasks ./src/main/kotlin/

# check the generated json files
check-jsons:
    jq empty {{generated}}/{{prefix}}inheritance-report.json
    jq empty {{generated}}/{{prefix}}outlier-report.json
    jq empty {{generated}}/{{prefix}}search-report.json
    jq empty {{generated}}/{{prefix}}packages-report.json
    jq empty {{generated}}/{{prefix}}imports-report.json
    jq empty {{generated}}/{{prefix}}imports-all-report.json
    jq empty {{generated}}/{{prefix}}coupling-report.json
    jq empty {{generated}}/{{prefix}}coupling-all-report.json
    jq empty {{generated}}/{{prefix}}file-imports-report.json
    jq empty {{generated}}/{{prefix}}file-imports-all-report.json

    jq empty {{generated}}/{{prefix}}class-statistics.json
    jq empty {{generated}}/{{prefix}}file-statistics.json
    jq empty {{generated}}/{{prefix}}package-statistics.json

# convert a mermaid file to svg
mermaid-to-svg FILE:
    @npx mmdc -i {{FILE}}

# convert a mermaid file to html
mermaid-convert FILE OUTPUT:
    @npx mmdc -i {{FILE}} -o {{OUTPUT}}
