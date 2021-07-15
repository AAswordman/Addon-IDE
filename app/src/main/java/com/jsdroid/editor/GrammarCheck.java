package com.jsdroid.editor;
import android.graphics.Path;
import android.graphics.Paint;
import bms.helper.tools.LOG;
import java.util.regex.Pattern;
import android.graphics.Color;
import android.graphics.Canvas;
import org.json.JSONObject;
import com.jsdroid.editor.GrammarCheck.ShowError;
import org.json.JSONException;
import bms.helper.app.EXPHelper;
import bms.helper.script.JavaScriptTools;
import bms.helper.script.JavaScriptTools.JSException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import bms.helper.tools.StringBuilderMemory;
import java.util.regex.Matcher;

public class GrammarCheck {

    public static class ShowError extends Exception {

        public int line;

        public int point;

        private String msg;

        public ShowError(int line, int point) {
            this.line = line;
            this.point = point;
        }
        public ShowError setMessage(String msg) {
            this.msg = msg;
            return this;
        }

    }
    public static interface Check {
        void prepare(boolean complete);
        void setPaint(String s, float fontWidths, Paint mPaint);
        void nextLine(float xOffset, float lbaseline);
        void publicTest(String msg) throws Exception;
        ShowError getError();
    }


    public static class JSON implements Check {

        private GrammarCheck.ShowError error;

        @Override
        public GrammarCheck.ShowError getError() {
            return this.error;
        }


        public static final int
        CODE_K            = 0,
        CODE_K_S          = 1,
        CODE_F_K          = 2,
        CODE_K_to_V       = 3,
        CODE_V            = 4,
        CODE_F_V          = 5,
        CODE_V_S          = 6,
        CODE_V_I          = 7,
        CODE_V_D          = 8,
        CODE_V_B_T        = 9,
        CODE_V_B_F        = 10,
        CODE_V_to_N       = 11,
        CODE_SET_FLAG     = 12;

        public static final char V_to_N=',';
        public static final char K_to_V=':';
        public static final char FLAG_NEW='{';
        public static final char FLAG_END='}';
        public static final char FLAG_ARR='[';
        public static final char FLAG_ARRE=']';
        public static final char FLAG_NO=' ';
        public static final char FLAG_NEXTLINE='\n';
        public static final char FLAG_STRING='"';

        public static final int SYSTEM_OBJECT=0;
        public static final int SYSTEM_ARRAY=1;

        private boolean isComplete=true,surfaceComplete=true;
        public static JSONObject keys=new JSONObject();

        public static void parseJson(String j) throws NoSuchFieldException, GrammarCheck.ShowError, IllegalAccessException, IllegalArgumentException {
            /*
             final Field field = String.class.getDeclaredField("value");
             field.setAccessible(true);
             final char[] chars = (char[]) field.get(j);
             */

            final char[] chars = j.toCharArray();
            final int len = chars.length;
            //LOG.print("反人类长度",len+"");
            int shouldState=CODE_F_V;

            int index=1;
            int row=0;
            boolean trans=false;
            int system = 0;
            StringBuilder sb = null;
            ArrayList<Integer> systemList=new ArrayList<>();
            systemList.add(SYSTEM_ARRAY);
            for (int i = 0; i < len; i++) {
                final char c=chars[i];
                row++;
                switch (c) {
                        case FLAG_NEW:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                system = SYSTEM_OBJECT;
                                systemList.add(system);
                                if (shouldState == CODE_SET_FLAG || shouldState == CODE_V || shouldState == CODE_F_V) {
                                    shouldState = CODE_F_K;
                                } else {
                                    throw new ShowError(index, row).setMessage("当前所需要状态:" + shouldState);
                                }
                            }
                            continue;
                        }
                        case FLAG_ARR:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                system = SYSTEM_ARRAY;
                                systemList.add(system);
                                if (shouldState == CODE_SET_FLAG || shouldState == CODE_V || shouldState == CODE_F_V) {
                                    shouldState = CODE_F_V;
                                } else {
                                    throw new ShowError(index, row).setMessage("当前所需要状态:" + shouldState);
                                }
                            }
                            continue;
                        }
                        case FLAG_STRING:{
                            if (!trans) {
                                if (shouldState == CODE_K_S || shouldState == CODE_F_K) {
                                    shouldState = CODE_K;
                                } else if (shouldState == CODE_K) {
                                    shouldState = CODE_K_to_V;


                                } else if (shouldState == CODE_V || shouldState == CODE_F_V) {
                                    shouldState = CODE_V_S;
                                } else if (shouldState == CODE_V_S) {
                                    shouldState = CODE_V_to_N;
                                } else {
                                    throw new ShowError(index, row).setMessage("当前所需要状态:" + shouldState);
                                }
                                continue;
                            }
                            break;
                        }
                        case K_to_V:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                if (shouldState == CODE_K_to_V) {
                                    shouldState = CODE_V;
                                } else {
                                    throw new ShowError(index, row).setMessage("当前所需要状态:" + shouldState);
                                }
                                continue;
                            }
                            break;
                        }
                        case V_to_N:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                switch (system) {
                                        case SYSTEM_OBJECT:{
                                            if (shouldState == CODE_V_to_N || shouldState == CODE_V_I || shouldState == CODE_V_D) {
                                                shouldState = CODE_K_S;
                                            } else {
                                                throw new ShowError(index, row).setMessage("错误使用','");
                                            }
                                            break;
                                        }
                                        case SYSTEM_ARRAY:{
                                            if (shouldState == CODE_V_to_N || shouldState == CODE_V_I || shouldState == CODE_V_D) {
                                                shouldState = CODE_V;
                                            } else {
                                                throw new ShowError(index, row).setMessage("错误使用','");
                                            }
                                            break;
                                        }
                                }
                                continue;
                            }
                            break;
                        }
                        case FLAG_END:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                if ((shouldState == CODE_V_to_N || shouldState == CODE_F_K || shouldState == CODE_V_I || shouldState == CODE_V_D) 
                                    && system == SYSTEM_OBJECT) {
                                    systemList.remove(systemList.size() - 1);
                                    if (systemList.size() - 1 < 0)throw new ShowError(index, row).setMessage("多出的'}'符号，无法和前面配对" + shouldState + ",当前system" + system);
                                    system = systemList.get(systemList.size() - 1);

                                    shouldState = CODE_V_to_N;
                                } else {
                                    throw new ShowError(index, row).setMessage("错误使用'}'");
                                }
                            }
                            continue;
                        }
                        case FLAG_ARRE:{
                            if (shouldState != CODE_K && shouldState != CODE_V_S) {
                                if ((shouldState == CODE_V_to_N || shouldState == CODE_F_V || shouldState == CODE_V_I || shouldState == CODE_V_D) 
                                    && system == SYSTEM_ARRAY) {
                                    systemList.remove(systemList.size() - 1);
                                    if (systemList.size() - 1 < 0)throw new ShowError(index, row).setMessage("多出的']'符号，无法和前面配对");
                                    system = systemList.get(systemList.size() - 1);
                                    shouldState = CODE_V_to_N;
                                } else {
                                    throw new ShowError(index, row).setMessage("错误使用']'");
                                }
                            }
                            continue;
                        }
                        case FLAG_NEXTLINE:{
                            index++;
                            //row = 0;
                            continue;
                        }
                        case FLAG_NO:{

                            continue;
                        }
                }

                //此处应该是STR内容或者数字内容
                if (shouldState != CODE_V_S && shouldState != CODE_K) {

                    //数字或者布尔值
                    if ('0' <= c && c <= '9' && (shouldState == CODE_V_I || (shouldState == CODE_V || shouldState == CODE_F_V))) {
                        shouldState = CODE_V_I;
                    } else if ((c == '.') && shouldState == CODE_V_I) {
                        shouldState = CODE_V_D;
                    } else if (shouldState == CODE_V_D && '0' <= c && c <= '9') {

                    } else if ((shouldState == CODE_V_I || (shouldState == CODE_V || shouldState == CODE_F_V)) && c == '-') {

                    } else 

                    if ((shouldState == CODE_V || shouldState == CODE_F_V) && c == 't') {
                        shouldState = CODE_V_B_T;
                        sb = new StringBuilder();
                        sb.append(c);
                    } else if ((shouldState == CODE_V || shouldState == CODE_F_V) && c == 'f') {
                        shouldState = CODE_V_B_F;
                        sb = new StringBuilder();
                        sb.append(c);
                    } else if (shouldState == CODE_V_B_F) {
                        sb.append(c);
                        if (sb.length() == 5) {
                            if (sb.toString().equals("false")) {
                                shouldState = CODE_V_to_N;
                            } else {
                                throw new ShowError(index, row).setMessage("不是正确的布尔值false");
                            }
                        }
                    } else if (shouldState == CODE_V_B_T) {
                        sb.append(c);
                        if (sb.length() == 4) {
                            if (sb.toString().equals("true")) {
                                shouldState = CODE_V_to_N;
                            } else {
                                throw new ShowError(index, row).setMessage("不是正确的布尔值true");
                            }
                        }
                    } else {
                        throw new ShowError(index, row).setMessage("不是正确的数字或布尔值");
                    }
                    //LOG.print("状态",shouldState+"");
                } else {


                    if (c == '\\' && !trans) {
                        trans = true;
                    } else if (trans) {
                        trans = false;
                    }
                }
            }



        }


        @Override
        public void publicTest(String msg) throws Exception{
            if (isComplete) {
                isComplete = false;

                try {
                    parseJson(msg);
                } catch (IllegalAccessException|IllegalArgumentException|NoSuchFieldException e) {

                } catch (GrammarCheck.ShowError e) {

                    this.error = e;
                    isComplete = true;
                    return;
                }
                isComplete = true;
                this.error = null;
            } else {

            }

        }

        private Paint mPath=new Paint();

        private boolean complete;
        private float baseLine;
        //public JavaScriptTools jst=new JavaScriptTools();
        private Pattern p=Pattern.compile("^ *([0-9]+(.?[0-9]+|)|\".*\"|-|,|\\[|\\]|\\{|\\}|\\:| *) *$");

        private float xOffset;


        private Paint mPaint;
        @Override
        public void prepare(boolean complete) {
            mPath.reset();
            this.complete = complete;

            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(15);
            mPaint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void setPaint(String s, float fontWidths, Paint mPaint) {
            /*
             if (!p.matcher(s).find()) {
             //canvas.drawLine( xOffset, baseLine,xOffset+fontWidths,baseLine,mPaint);
             mPaint.setUnderlineText(true);
             mPaint.setColor(Color.RED);
             }else{
             mPaint.setUnderlineText(false);
             }
             */

            xOffset += fontWidths;
        }

        @Override
        public void nextLine(float xOffset, float lbaseline) {

            this.xOffset = xOffset;
            this.baseLine = lbaseline;
        }

        public String getSurface(CharSequence jsonPart) throws Exception{
            if (jsonPart.length() == 0) {
                return null;
            }
            if (surfaceComplete) {
                surfaceComplete = false;

                long startTime = System.currentTimeMillis();

                /*
                 StringBuilderMemory sb=new StringBuilderMemory();
                 sb.append(jsonPart);
                 sb.replace(" ", "");
                 sb.replace("\n", "");
                 Pattern p=Pattern.compile("(?:,?\"(?:(?:\\\\\"|[^\"])+|)\":(?:\"(?:(?:\\\\\"|[^\"])+|)\"|-?\\d+\\.?\\d*|true|false|\\{\\}|\\[(?:-?\\d+\\.?\\d*|true|false|\"(?:(?:\\\\\"|[^\"])+|)\"|,)+\\]),?)");
                 Matcher m=p.matcher(sb.getBuilder());
                 while (m.find()) {
                 sb.replace(m.group(0), "");
                 m = p.matcher(sb.getBuilder());
                 }
                 */

                ArrayList<String> keyList=new ArrayList<>();
                boolean trans=false;
                ArrayList<Integer> systemList=new ArrayList<>();
                boolean inStr = false;
                final char[] str=jsonPart.toString().toCharArray();
                StringBuilderMemory s=new StringBuilderMemory(),
                    keyBuilder=new StringBuilderMemory();
                String keyCache = "";
                boolean isEmpty=false;

                for (final char c : str) {
                    switch (c) {
                            case '{':{
                                if (!inStr) {
                                    if (keyCache != null) {
                                        s.append("/");
                                        keyCache=null;
                                    } else {
                                        s.append("/");
                                    }
                                    systemList.add(-1);
                                    isEmpty=true;
                                    //LOG.print("添加",s.toString());
                                }
                                continue;
                            }

                            case '[':{
                                if (!inStr) {
                                    if (keyCache != null) {
                                        
                                        s.append("/");
                                        
                                        keyCache=null;
                                    } else {
                                        s.append("/");
                                        
                                    }
                                    systemList.add(0);
                                    
                                    keyCache=0+"";
                                    s.append(keyCache);
                                }
                                //LOG.print("添加",s.toString());
                                continue;
                            }
                            case '"':{
                                if (!trans) {
                                    if (inStr) {
                                        inStr = false;
                                        keyCache = keyBuilder.toString();
                                        keyBuilder = new StringBuilderMemory();
                                        
                                        
                                    } else {
                                        
                                        inStr = true;
                                    }

                                    continue;
                                }
                                break;
                            }
                            case ':':{
                                if(!inStr){
                                    isEmpty=false;
                                    if (keyCache != null) {
                                        s.append(keyCache);
                                        keyCache=null;
                                    } else {
                                        s.append("" + systemList.get(systemList.size() - 1));
                                    }
                                    
                                    
                                }
                                break;
                            }
                            case ',':{
                                if(!inStr){
                                    int nowKey=systemList.get(systemList.size() - 1);
                                    if (nowKey != -1) {
                                        systemList.set(systemList.size() - 1, nowKey + 1);
                                        keyCache=(nowKey+1)+"";
                                        s.revoke();
                                        s.append(keyCache);
                                    }else{
                                        s.revoke();
                                    }
                                }
                                break;
                            }
                            case '}':{
                                if (!inStr) {
                                    systemList.remove(systemList.size() - 1);
                                    s.revoke();
                                    if(!isEmpty)s.revoke();
                                    isEmpty=false;
                                }
                                //LOG.print("减少",s.toString());
                                continue;
                            }
                            case ']':{
                                if (!inStr) {
                                    systemList.remove(systemList.size() - 1);
                                    s.revoke().revoke();
                                    
                                }
                                //LOG.print("减少",s.toString());
                                continue;
                            }
                    }
                    //str
                    if (inStr) {
                        keyBuilder.append(c + "");
                    }
                    if (c == '\\' && !trans) {
                        trans = true;
                    } else if (trans) {
                        trans = false;
                    }
                }
                long endTime = System.currentTimeMillis();
                surfaceComplete = true;
                //LOG.print("时间", (endTime - startTime) + "");
                
                return s.toString();

            }
            return null;
        }
    }


}
