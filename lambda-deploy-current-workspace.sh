#!/bin/sh

mvn clean install && \
aws lambda update-function-code \
    --function-name SecretSantaBroadcastHandler \
    --zip-file fileb://./target/SecretSantaMessager-1.0-SNAPSHOT.jar

