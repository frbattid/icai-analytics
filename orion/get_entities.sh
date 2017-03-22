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

ENTITY=$1

curl -v -X GET "http://localhost:1026/v2/entities/$ENTITY?limit=50" -H "Fiware-Service: icai" -H "Fiware-ServicePath: /neighbourhood"
