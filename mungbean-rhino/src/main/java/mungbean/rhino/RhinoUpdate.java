package mungbean.rhino;

import mungbean.query.Update;

import org.mozilla.javascript.ScriptableObject;

public class RhinoUpdate extends Update {

    public RhinoUpdate(ScriptableObject data, ScriptableObject options) {
        super(RhinoQuery.toMap(data), RhinoQuery.toMap(options));
    }
}
