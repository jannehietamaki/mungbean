package mungbean.rhino;

import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONArray;
import mungbean.protocol.bson.BSONBoolean;
import mungbean.protocol.bson.BSONDate;
import mungbean.protocol.bson.BSONEndMarker;
import mungbean.protocol.bson.BSONInteger;
import mungbean.protocol.bson.BSONLong;
import mungbean.protocol.bson.BSONMap;
import mungbean.protocol.bson.BSONNull;
import mungbean.protocol.bson.BSONNumber;
import mungbean.protocol.bson.BSONOid;
import mungbean.protocol.bson.BSONPattern;
import mungbean.protocol.bson.BSONString;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RhinoBSONCoders extends AbstractBSONCoders {
    public RhinoBSONCoders(Context context, Scriptable scriptable) {
        addEncoder(new BSONEndMarker());
        addEncoder(new BSONNull());
        addEncoder(new RhinoBSONList<Object>(scriptable));
        addEncoder(new BSONArray<Object>());
        addEncoder(new BSONInteger());
        addEncoder(new BSONLong());
        addEncoder(new BSONNumber());
        addEncoder(new BSONString());
        addEncoder(new BSONOid());
        addEncoder(new BSONPattern());
        addEncoder(new RhinoBSONDate(scriptable, context));
        addEncoder(new BSONDate());
        addEncoder(new BSONBoolean());
        addEncoder(new RhinoBSONMap());
        addEncoder(new BSONMap());
    }
}
