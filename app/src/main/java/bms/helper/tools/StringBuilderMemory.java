package bms.helper.tools;
import java.util.Stack;
import android.text.Editable;
import android.text.InputFilter;

public class StringBuilderMemory implements CharSequence ,Editable {

    @Override
    public void getChars(int p1, int p2, char[] p3, int p4) {
    }

    @Override
    public Editable replace(int p1, int p2, CharSequence p3, int p4, int p5) {
        return this;
    }

    @Override
    public Editable replace(int p1, int p2, CharSequence p3) {
        
        return this;
    }

    @Override
    public Editable insert(int p1, CharSequence p2, int p3, int p4) {
        return this;
    }

    @Override
    public Editable insert(int p1, CharSequence p2) {
        return this;
    }

    @Override
    public Editable delete(int p1, int p2) {
        return this;
    }

    @Override
    public Editable append(CharSequence p1, int p2, int p3) {
        return this;
    }

    @Override
    public void clear() {
    }

    @Override
    public void clearSpans() {
    }

    @Override
    public void setFilters(InputFilter[] p1) {
    }

    @Override
    public InputFilter[] getFilters() {
        return null;
    }

    @Override
    public void setSpan(Object p1, int p2, int p3, int p4) {
    }

    @Override
    public void removeSpan(Object p1) {
    }

    @Override
    public <T extends Object> T[] getSpans(int p1, int p2, Class<T> p3) {
        return null;
    }

    @Override
    public int getSpanStart(Object p1) {
        return 0;
    }

    @Override
    public int getSpanEnd(Object p1) {
        return 0;
    }

    @Override
    public int getSpanFlags(Object p1) {
        return 0;
    }

    @Override
    public int nextSpanTransition(int p1, int p2, Class p3) {
        return 0;
    }
    

    @Override
    public int length() {
        return builder.length();
    }

    @Override
    public char charAt(int p1) {
        return builder.charAt(p1);
    }

    @Override
    public CharSequence subSequence(int p1, int p2) {
        return builder.subSequence(p1,p2);
    }
    
    private StackWithMax<Integer> memory=new StackWithMax<>(Integer.MAX_VALUE);
    private StringBuilder builder=new StringBuilder();

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
        
    }

    public StringBuilder getBuilder() {
        return builder;
    }
    public StringBuilderMemory append(CharSequence c) {
        //LOG.print("加入",c.toString());
        builder.append(c);
        memory.push(c.length());
        return this;
    }
    public StringBuilderMemory append(char c) {
        //LOG.print("加入",c+"");
        builder.append(c);
        memory.push(1);
        return this;
    }
    public StringBuilderMemory append(java.lang.String str) {
        //LOG.print("加入",str);
        builder.append(str);
        memory.push(str.length());
        return this;
    }
    public StringBuilderMemory revoke() {
        //LOG.print("撤回","");
        builder.delete(builder.length()- memory.pop(), builder.length());
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
    
    public StringBuilderMemory replace(int start, int end, java.lang.String str) {
        builder.replace(start, end, str);
        return this;
    }

    public static String fastReplace(String str, String target, String replacement) {
        int targetLength = target.length();
        if (targetLength == 0) {
            return str;
        }
        int idx2 = str.indexOf(target);
        if (idx2 < 0) {
            return str;
        }
        StringBuilder buffer = new StringBuilder(targetLength > replacement.length() ? str.length() : str.length() * 2);
        int idx1 = 0;
        do {
            buffer.append(str, idx1, idx2);
            buffer.append(replacement);
            idx1 = idx2 + targetLength;
            idx2 = str.indexOf(target, idx1);
        } while( idx2 > 0 );
        buffer.append(str, idx1, str.length());
        return buffer.toString();
    }

    public StringBuilderMemory replace(final String searchChars, String replaceChars) {
        if (this.builder.length() == 0 || "".equals(searchChars) || searchChars.equals(replaceChars)) {
            return this;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        final int strLength = builder.length();
        final int searchCharsLength = searchChars.length();
        StringBuilder buf = builder;
        boolean modified = false;

        for (int i = 0; i < strLength; i++) {
            int start = buf.indexOf(searchChars, i);
            if (start == -1) {
                if (i == 0) {
                    return this;
                }
                return this;
            }
            buf = buf.replace(start, start + searchCharsLength, replaceChars);
            modified = true;
        }

        
        return this;


    }
}
