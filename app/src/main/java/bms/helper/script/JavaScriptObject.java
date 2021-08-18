package bms.helper.script;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;

public class JavaScriptObject {
    private NativeObject obj;
    private JavaScriptTools tool;

    public JavaScriptObject(JavaScriptTools var1, NativeObject var2) {
        this.tool = var1;
        this.obj = var2;
    }

    public Object get(String var1) {
        return this.obj.get(var1, this.tool.scope);
    }

    public JavaScriptFunction getFunction(String var1) {
        return new JavaScriptFunction(this.tool, (Function)this.get(var1));
    }

    public JavaScriptObject getObject(String var1) {
        return new JavaScriptObject(this.tool, (NativeObject)this.get(var1));
    }

    public boolean has(String var1) {
        return this.obj.has(var1, this.tool.scope);
    }

    public boolean hasFunction(String var1) {
        return this.get(var1) instanceof Function;
    }

    public boolean hasObject(String var1) {
        return this.get(var1) instanceof NativeObject;
    }
}


