/*
   Copyright 2009 Janne Hietamäki

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

package mungbean.protocol.bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractBSONCoders {

    private final List<BSONCoder<?>> encoders = new ArrayList<BSONCoder<?>>();

    protected void addEncoder(BSONCoder<?> coder) {
        encoders.add(coder);
    }

    public BSONCoder<?> forType(byte type) {
        for (BSONCoder<?> bson : encoders) {
            if (bson.type() == type) {
                return bson;
            }
        }
        throw new IllegalArgumentException("Unsupported BSON type " + type);
    }

    @SuppressWarnings("unchecked")
    public <T> BSONCoder<T> forValue(T val) {
        for (BSONCoder<?> bson : encoders) {
            if (bson.canEncode(val)) {
                return (BSONCoder<T>) bson;
            }
        }
        throw new IllegalArgumentException(getClass().getSimpleName() + ": Unable to find encoder for object " + val + " (" + val.getClass().getName() + "/" + Arrays.asList(val.getClass().getInterfaces()) + ")");
    }
}