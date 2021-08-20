package bms.helper.script;

import java.io.FileReader;
import java.io.IOException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import bms.helper.tools.LOG;
import java.util.ArrayList;
import bms.helper.app.EXPHelper;
import bms.helper.script.JavaScriptTools.JSException;
import java.util.HashMap;
import chineseframe.CFile;
import bms.helper.io.CreateFile;
import bms.helper.tools.StringBuilderMemory;

public class JavaScriptTools {
    Context cx;
    Function fct;
    ScriptableObject scope;
    Scriptable that;
    String impPath;
    static HashMap<String,JavaScriptTools> pool=new HashMap<>();
    static HashMap<String,String> jsMap=new HashMap<>();
    String filePath;
    private boolean normal;
    private boolean exit;

    /*
     public JavaScriptTools(String impPath) {
     this.impPath = impPath;
     }
     */
    public static JavaScriptTools getTools(String impPath, String var1) throws JavaScriptTools.JSException {
        String k=impPath + ":" + var1;
        if (pool.containsKey(k)) {
            return pool.get(k);
        } else {
            JavaScriptTools js=new JavaScriptTools(impPath, var1);
            pool.put(k, js);
            return js;
        }

    }
    public JavaScriptTools() {
        loadjs();

    }
    private JavaScriptTools(String impPath, String var1) throws JavaScriptTools.JSException {
        this.impPath = impPath;
        this.loadjs(var1);
    }

    public Object eval(String var1) throws JavaScriptTools.JSException {
        if (normal) {
            if (exit)return null;
            try {
                return this.cx.evaluateString(this.scope, var1, "script", 0, (Object)null);
            } catch (Exception e) {
                kill();
                throw new JSException(jsMap.get(filePath), e);
            }
        } else {
            return this.cx.evaluateString(this.scope, var1, "script", 0, (Object)null);
        }
    }

    public Object get(String var1) {
        return this.scope.get(var1, this.scope);
    }

    public JavaScriptFunction getFunction(String var1) {
        return new JavaScriptFunction(this, (Function)this.get(var1));
    }

    public JavaScriptObject getObject(String var1) {
        return new JavaScriptObject(this, (NativeObject)this.get(var1));
    }

    public boolean has(String var1) {
        return this.scope.has(var1, this.scope);
    }

    public boolean hasFunction(String var1) {
        return this.get(var1) instanceof Function;
    }

    public boolean hasObject(String var1) {
        return this.get(var1) instanceof NativeObject;
    }

    public void kill() {
        exit = true;
        Context.exit();
    }
    public void loadjs() {
        normal = false;
        this.cx = Context.enter();
        try {
            this.scope = this.cx.initStandardObjects();
            this.cx.setOptimizationLevel(-1);
            this.that = this.cx.newObject(this.scope);
            this.cx.setLanguageVersion(180);

        } catch (Exception e) {
            kill();

        } finally {
            ;}
    }
    public void loadjs(String var1) throws JavaScriptTools.JSException {
        normal = true;
        this.cx = Context.enter();
        filePath = var1;
        try {
            this.scope = this.cx.initStandardObjects();
            this.cx.setOptimizationLevel(-1);
            this.that = this.cx.newObject(this.scope);
            this.cx.setLanguageVersion(180);


            Context var3 = this.cx;
            ScriptableObject var4 = this.scope;
            StringBuffer var5 = new StringBuffer();
            //FileReader var2 = new FileReader(var5.append("/sdcard/").append(var1).toString());
            //var3.evaluateReader(var4, var2, "script", 0, (Object)null);
            String str = null;
            if (jsMap.containsKey(filePath)) {
                str = jsMap.get(filePath);
            } else {

                try {
                    str = new CFile("/sdcard/" + var1).read();
                } catch (IOException e) {}
                ArrayList<String> already=new ArrayList<>();
                already.add(var1);
                Pattern p=Pattern.compile("import (.*);");

                Matcher m=p.matcher(str);
                while (m.find()) {
                    String path=impPath + m.group(1).replace(".", "/") + ".js";
                    if (already.contains(path)) {
                        str = str.replace(m.group(0), "");
                    } else {
                        try {
                            str = StringBuilderMemory.fastReplace(str,m.group(0),new CFile("/sdcard/" + path).read());
                        } catch (IOException e) {}
                    }
                    m = p.matcher(str);
                }
                //LOG.print("str", str);
                jsMap.put(filePath, str);
            }
            try {
                var3.evaluateString(var4, str, "script", 0, null);
            } catch (Exception e) {
                kill();
                throw new JSException(str, e);
            }

        } finally {
            ;
        }
    }
    public static class JSException extends Exception {
        private String message;
        private String[] js;
        public JSException() {
            super();
        }
        public JSException(String js, Exception e) {
            message = EXPHelper.get(e);
            this.js = js.split("\n");
        }

        @Override
        public String getMessage() {
            StringBuilder msg=new StringBuilder();
            Pattern p=Pattern.compile("org.mozilla.javascript.*\\(script#([0-9]*)\\)");
            Matcher m=p.matcher(message);
            if (m.find()) {
                msg.append(m.group(0));
                msg.append("\nThe Error is:\n");
                getJs(m.group(1), msg);
            }

            p = Pattern.compile("at script.*script:([0-9])*\\)");
            m = p.matcher(message);
            while (m.find()) {
                msg.append("\nfrom:\n");
                getJs(m.group(1), msg);

            }
            msg.append("\n");
            return msg.toString();
        }
        private void getJs(String num, StringBuilder sb) {
            int n=Integer.parseInt(num);
            sb.append("    ").append(js[Math.max(0, n - 1)].trim())
                .append("\n")
                .append("    ").append(js[Math.max(0, n)].trim()).append("")
                .append("\n    ").append(js[Math.max(0, n + 1)].trim());

        }
        public void writeTo(String p) {
            CreateFile.WriteAppend("/sdcard/" + p, getMessage());
        }
        public void writeToT(String p) {
            CreateFile.WriteAppend("/sdcard/" + p, message);
        }

    }
}

