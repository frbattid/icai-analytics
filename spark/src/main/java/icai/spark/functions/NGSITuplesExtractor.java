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
package icai.spark.functions;

import com.google.gson.Gson;
import icai.gson.containers.NotifyContextRequest.ContextAttribute;
import icai.gson.containers.NotifyContextRequest.ContextElement;
import java.util.ArrayList;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;
import icai.gson.containers.NotifyContextRequest.ContextElementResponse;

/**
 * Extracts tuples from a string containing NGSI data in Json format.
 * 
 * @author frbattid
 */
public class NGSITuplesExtractor implements FlatMapFunction<
        String,
        Tuple2<NGSITuple, Float>> {

    @Override
    public Iterable<Tuple2<NGSITuple, Float>> call(String str) {
        ArrayList tuples = new ArrayList<>();
        Gson gson = new Gson();
        ContextElementResponse cer = gson.fromJson(str, ContextElementResponse.class);
        ContextElement ce = cer.getContextElement();
        String entityId = ce.getId();
        String entityType = ce.getType();

        for (ContextAttribute ca : ce.getAttributes()) {
            String attrName = ca.getName();
            String attrType = ca.getType();
            NGSITuple key = new NGSITuple(entityId, entityType, attrName, attrType);
            Float value = new Float(ca.getContextValue(false));
            tuples.add(new Tuple2(key, value));
        } // for

        return tuples;
    } // call
} // NGSITuplesExtractor
