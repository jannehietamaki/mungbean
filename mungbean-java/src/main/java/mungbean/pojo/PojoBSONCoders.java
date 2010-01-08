package mungbean.pojo;

import mungbean.protocol.bson.MapBSONCoders;

public class PojoBSONCoders<T> extends MapBSONCoders {

    public PojoBSONCoders(Class<T> typeClass) {
        addEncoder(new PojoEncoder<T>(typeClass));
    }
}
