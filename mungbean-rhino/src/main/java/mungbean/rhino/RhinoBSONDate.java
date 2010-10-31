package mungbean.rhino;

import java.util.Date;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONCoder;

public class RhinoBSONDate extends BSONCoder<IdScriptableObject> {

    private final Scriptable scriptable;
    private final Context context;

    protected RhinoBSONDate(Scriptable scriptable, Context context) {
        super(9, IdScriptableObject.class);
        this.scriptable = scriptable;
        this.context = context;
    }

    public boolean canEncode(Object val) {
        return val != null && NativeJavaObject.canConvert(val, Date.class);
    }

    @Override
    protected IdScriptableObject decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
        long value = reader.readLong();
        Object[] args = { value };
        return (IdScriptableObject) context.newObject(scriptable, "Date", args);
    }

    @Override
    protected void encode(AbstractBSONCoders bson, IdScriptableObject o, LittleEndianDataWriter writer) {
        Date date = (Date) Context.jsToJava(o, Date.class);
        writer.writeLong(date.getTime());
    }
}
