#!/bin/sh

INPUT_JSON_FILE=$1
INPUT_JSON=`<$INPUT_JSON_FILE`

RESPONSE_JSON_FILE=/tmp/invoke_output.txt
aws lambda invoke \
    --function-name SecretSantaBroadcastHandler \
    --cli-binary-format raw-in-base64-out \
    --payload "$INPUT_JSON" \
    $RESPONSE_JSON_FILE \
&& echo -e "\nResponse payload:" \
&& cat $RESPONSE_JSON_FILE | jq \
&& rm $RESPONSE_JSON_FILE \
&& echo
