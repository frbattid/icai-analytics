#!/bin/bash

curl -v -X POST "http://localhost:1026/v2/subscriptions" -H "Fiware-Service: icai" -H "Fiware-ServicePath: /neighbourhood" -H "Content-Type: application/json" -d @- <<EOF
{
  "subject": {
    "entities": [
      {
        "idPattern": "swm.*",
        "type": "smartWaterMeter"
      }
    ]
  },
  "notification": {
    "http": {
      "url": "http://localhost:5050/notify"
    },
    "attrsFormat": "legacy"
  }
}
EOF
