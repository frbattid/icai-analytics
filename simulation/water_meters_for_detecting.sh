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
