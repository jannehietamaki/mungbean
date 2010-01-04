/*
   Copyright 2009-2010 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package mungbean.protocol.command;

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.DBCollection;
import mungbean.protocol.bson.Code;
import mungbean.protocol.message.CommandResponse;
import mungbean.query.QueryBuilder;

public class MapReduce extends Aggregation<MapReduceResult> {
    private final String map;
    private final String reduce;
    private String outputCollection = null;
    private Boolean keepTemp = null;
    private String finalizeFunction = null;
    private Boolean verbose = null;

    public MapReduce(String map, String reduce) {
        this.map = map;
        this.reduce = reduce;
    }

    public void setOutputCollection(String outputCollection) {
        this.outputCollection = outputCollection;
    }

    public void setKeepTemp(boolean keepTemp) {
        this.keepTemp = keepTemp;
    }

    public void setFinalizeFunction(String finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public MapReduceResult parseResponse(CommandResponse values) {
        return new MapReduceResult(values);
    }

    @Override
    public Map<String, Object> requestMap(DBCollection<?> collection, QueryBuilder query) {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("mapreduce", collection.collectionName());
        request.put("map", new Code(map));
        request.put("reduce", new Code(reduce));
        if (!query.build().isEmpty()) {
            request.put("query", query.build());
        }
        if (!query.order().isEmpty()) {
            request.put("sort", query.order());
        }
        if (query.limit() > 0) {
            request.put("limit", query.limit());
        }
        if (outputCollection != null) {
            request.put("out", outputCollection);
        }
        if (keepTemp != null) {
            request.put("keeptemp", keepTemp);
        }
        if (finalizeFunction != null) {
            request.put("finalize", new Code(finalizeFunction));
        }
        if (verbose != null) {
            request.put("verbose", verbose);
        }
        return request;
    }
}
