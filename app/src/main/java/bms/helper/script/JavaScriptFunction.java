package bms.helper.script;

import org.mozilla.javascript.Function;
import bms.helper.script.JavaScriptTools.JSException;

public class JavaScriptFunction {
    private Function obj;
    private JavaScriptTools tool;

    public JavaScriptFunction(JavaScriptTools javaScriptTools, Function function) {
        this.tool = javaScriptTools;
        this.obj = function;
    }

    public Object call(Object[] objArr) throws JavaScriptTools.JSException {
        try {
            return this.obj.call(this.tool.cx, this.tool.scope, this.tool.that, objArr);
        } catch (Exception e) {
            tool.kill();
            throw new JavaScriptTools.JSException(tool.jsMap.get(tool.filePath),e);
        }
    }
}

