/**
 * Copyright 2017 Francisco Romero Bueno
 *
 * icai-demo is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * icai-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with icai-demo. If not, see
 * http://www.gnu.org/licenses/.
 *
 * For those usages not covered by the GNU Affero General Public License please raise an issue at
 * https://github.com/frbattid/icai-demo
 */
package icai.analytics;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import scala.Tuple2;
import icai.spark.functions.AvroDeserializer;
import icai.spark.functions.HashMapModeler;
import icai.spark.functions.NGSITuple;
import icai.spark.functions.NGSITuplesAggregatorAll;
import icai.spark.functions.NGSITuplesAggregatorByKey;
import icai.spark.functions.NGSITuplesExtractor;
import icai.spark.functions.NGSITupleToPair;

/**
 * Modeler application.
 * 
 * @author frbattid
 */
public final class Modeler {
    
    /**
     * Constructor. It is private since utility classes should not have a public or default constructor.
     */
    private Modeler() {
    } // Modeler
    
    /**
     * Main class for this modeler application.
     * @param args
     */
    public static void main(String[] args) {
        // Show usage
        if (args.length != 4) {
            System.err.println("Usage: icai.analytics.Modeler <host> <port> <models_base_path> <debug>");
            System.exit(1);
        } // if

        // Get parameters
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String modelsBasePath = args[2];
        boolean debug = args[3].equals("true");

        // Batch interval duration (1 minute)
        Duration batchInterval = new Duration(60000);

        // Create the streaming context
        SparkConf sparkConf = new SparkConf().setAppName("Modeler");
        JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, batchInterval);

        // Create the stream object from Avro binaries sent by Flume
        JavaReceiverInputDStream<SparkFlumeEvent> stream = FlumeUtils.createStream(ssc, host, port);

        // Get string-based events
        JavaDStream<String> streamStr = stream.map(new AvroDeserializer());
        
        if (debug) {
            streamStr.print();
        } // if

        // Get NGSI tuples
        JavaDStream<Tuple2<NGSITuple, Float>> tuples = streamStr.flatMap(new NGSITuplesExtractor());
        
        if (debug) {
            tuples.print();
        } // if
        
        // Sum all the NGSI tuples
        JavaDStream<Tuple2<NGSITuple, Float>> sumAll =
                tuples.reduce(new NGSITuplesAggregatorAll("aggregated", "model", "", ""));
        
        if (debug) {
            sumAll.print();
        } // if

        // Create the model for the entire neighbourhood and save it in a file
        JavaPairDStream<String, Float> sumAllAsPairs = sumAll.mapToPair(new NGSITupleToPair(true));
        sumAllAsPairs.toJavaDStream().foreachRDD(new HashMapModeler(modelsBasePath, 24, true));

        // Translate the NGSI tuples to a <String, Float> pair
        JavaPairDStream<String, Float> pairs = tuples.mapToPair(new NGSITupleToPair(true));
        
        if (debug) {
            pairs.print();
        } // if
        
        // Sum the <String, Float> pairs by key
        JavaPairDStream<String, Float> sumByKeyAsPairs = pairs.reduceByKey(new NGSITuplesAggregatorByKey());
        
        if (debug) {
            sumByKeyAsPairs.print();
        } // if
        
        // Create the model for each home and save it in a file
        sumByKeyAsPairs.toJavaDStream().foreachRDD(new HashMapModeler(modelsBasePath, 24, true));
        
        // Start the app
        ssc.start();
        System.out.println("Modeler started, ready to receive Avro events from Cygnus/Flume...");
        ssc.awaitTermination();
    } // main

} // Modeler
