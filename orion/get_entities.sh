#!/bin/bash

ENTITY=$1

curl -v -X GET "http://localhost:1026/v2/entities/$ENTITY?limit=50" -H "Fiware-Service: icai" -H "Fiware-ServicePath: /neighbourhood"
