#!/bin/sh

exit 1 # TODO

mvn clean install && \
aws lambda update-function-code \
    --function-name TODO \
    --zip-file fileb://./target/TODO-1.0-SNAPSHOT.jar
