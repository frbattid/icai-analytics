#!/bin/bash
#
# Copyright 2017 Francisco Romero Bueno
#
# icai-demo is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
# General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.
# 
# icai-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
# for more details.
#
# You should have received a copy of the GNU Affero General Public License along with icai-demo. If not, see
# http://www.gnu.org/licenses/.
#
# For those usages not covered by the GNU Affero General Public License please raise an issue at
# https://github.com/frbattid/icai-demo

# Create 50 smart water meters
COUNTER=1

while [ $COUNTER -lt 51 ]; do
        DATE=`date +%Y-%m-%d:%H:%M:%S`
        echo "[$DATE] Creating entity swm$COUNTER"
        curl -X POST \
                "http://localhost:1026/v2/entities" \
                -H "Fiware-Service: icai" \
                -H "Fiware-ServicePath: /neighbourhood" \
                -H "Content-Type: application/json" \
                -d '{"id": "swm'$COUNTER'", "type": "smartWaterMeter", "waterFlow": { "value": 0, "type": "float" } }' \
                > /dev/null 2> /dev/null
        let COUNTER=COUNTER+1
done

# Regularly update (every 5 seconds) the 50 smart water meters
# Smart water meter number X updates values in [X, X+1, X+2, X+3, X+4] range
while true; do
        COUNTER=1

        while [ $COUNTER -lt 51 ]; do
                VALUE=$(( $RANDOM % 5 + $COUNTER ))
                DATE=`date +%Y-%m-%d:%H:%M:%S`
                echo "[$DATE] Updating entity swm$COUNTER with value $VALUE"
                curl -X PUT \
                        "http://localhost:1026/v2/entities/swm$COUNTER/attrs/waterFlow/value" \
                        -H "Fiware-Service: icai" \
                        -H "Fiware-ServicePath: /neighbourhood" \
                        -H "Content-Type: text/plain" \
                        -d $VALUE
                let COUNTER=COUNTER+1
        done

        sleep 5
done

