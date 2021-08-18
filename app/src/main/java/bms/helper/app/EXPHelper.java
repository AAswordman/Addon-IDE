package bms.helper.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class EXPHelper {
    public static String get(Throwable e) {
        e.printStackTrace();
        StringWriter sw = new StringWriter();   
        PrintWriter pw = new PrintWriter(sw, true);   
        e.printStackTrace(pw);   
        pw.flush();   
        sw.flush();   
        return sw.toString();

    }
	public static String get(Exception e) {
		e.printStackTrace();
		StringWriter sw = new StringWriter();   
		PrintWriter pw = new PrintWriter(sw, true);   
		e.printStackTrace(pw);   
		pw.flush();   
		sw.flush();   
		return sw.toString();

    }
}
