#!/bin/bash

# Regularly update (every 5 seconds) a random smart water meter
# The selected smart water meter updates values in [2*X, 2*X+1, 2*X+2, 2*X+3, 2*X+4] range
SWM=$(( $RANDOM % 50 + 1 ))

while true; do
        VALUE=$(( $RANDOM % 5 + (2 * $SWM) ))
        DATE=`date +%Y-%m-%d:%H:%M:%S`
        echo "[$DATE] Updating entity swm$SWM with value $VALUE"
        curl -X PUT \
                "http://localhost:1026/v2/entities/swm$SWM/attrs/waterFlow/value" \
                -H "Fiware-Service: icai" \
                -H "Fiware-ServicePath: /neighbourhood" \
                -H "Content-Type: text/plain" \
                -d $VALUE \
                > /dev/null 2> /dev/null
        sleep 5
done
