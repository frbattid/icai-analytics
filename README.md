# icai-demo
Resources for a Big Data demo in the context of ICAI's Master "Communications for Smart Systems".

The repository is structured as follows:

* `cygnus/` contains the required configuration files for [FIWARE Cygnus](https://github.com/telefonicaid/fiware-cygnus) tool, in order to forward context notifications to Spark Streaming in the form of Avro events.
* `orion/` contains scripts for creating a subscription to [FIWARE Orion](https://github.com/telefonicaid/fiware-orion) Context Broker, in addition to a script for getting context entities information. Such entities are about 50 simulated smart water meters.
* `simulation/` contains scripts for simulating 50 smart water meters, either in modeling mode, either in detection mode.
* `spark/` contains Java code for a couple of [Spark Streaming](http://spark.apache.org/streaming/) analytics in charge of modeling the behaviour of the 50 smart water meters, and in charge of detecting anomalies in the water consumption.

## License
This is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

All files except `NotifyContextRequest.java` copyright by Francisco Romero Bueno.

`NotifyContextRequest.java` copyright by Telefonica Investigación y Desarrollo, S.A.U.
