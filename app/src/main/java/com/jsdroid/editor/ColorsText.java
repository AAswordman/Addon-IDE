package com.jsdroid.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import bms.helper.tools.LOG;
import bms.helper.tools.StringBuilderMemory;
import bms.helper.tools.TimeDelayer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import android.support.v7.widget.AppCompatEditText;

public class ColorsText extends AppCompatEditText {
    private Object colorLock = new Object();
    // 代码高亮颜色数组
    private int[] codeColors;
    // 滑动组件
    private View scrollView;
    // 最大行号：根据\n数量得到
    private int mMaxLineNumber;
    // 行号内边距
    private int mNumberPadding;
    private int mTextPadding;
    private int mLineNumberBgStrokeWidth;
    private int defaultTextColor = 0xffffffff;
    private int lineNumberColor = 0x99ffffff;
    private int lineNumberBackgroundColor = 0x99ffffff;
    private int lineNumberSplitColor = 0x99ffffff;
    private int lineNumberSplitWidth = 1;
    private int cursorColor = 0xffffffff;
    private int selectBackgroundColor = 0x33ffffff;

    private boolean rangDraw;

    private int[] lastLineRang;


    public void RemoveRangDraw() {
        rangDraw = false;
    }

    public void AddRangDraw() {
        this.rangDraw = true;
    }

    //语法检查
    protected GrammarCheck.Check check=new GrammarCheck.Check(){

        @Override
        public void publicTest(String msg) {
        }

        @Override
        public GrammarCheck.ShowError getError() {
            return null;
        }

        @Override
        public void prepare(boolean complete) {
        }

        @Override
        public void setPaint(String s, float fontWidths, Paint mPaint) {
        }

        @Override
        public void nextLine(float xOffset, float lbaseline) {
        }


    };
    //检查一次间隔
    private TimeDelayer delay=new TimeDelayer(200);
    //错误定位
    private GrammarCheck.ShowError errorShow;
    private GrammarCheck.ShowError errorShowSafe;
    public String jsonPath;

    private StringBuilderMemory text=new StringBuilderMemory();

    private float textSize;

    private Map<Integer, String> lineNumMap;

    protected int fixedHeight;

    protected int fixedWidth;

    private int lineCountCache;

    private int visualFirstLine;

    private int visualLastLine;

    private int mlineCountCache;

    public void setErrorShow(GrammarCheck.ShowError errorShow) {
        this.errorShow = errorShow;
    }

    public GrammarCheck.ShowError getErrorShow() {
        return errorShow;
    }


    public int getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public int getFixedHeight() {
        return fixedHeight;
    }


    public void setLineNumberColor(int lineNumberColor) {
        this.lineNumberColor = lineNumberColor;
    }

    public void setLineNumberBackgroundColor(int lineNumberBackgroundColor) {
        this.lineNumberBackgroundColor = lineNumberBackgroundColor;
    }

    public void setLineNumberSplitColor(int lineNumberSplitColor) {
        this.lineNumberSplitColor = lineNumberSplitColor;
    }

    public void setSelectBackgroundColor(int selectBackgroundColor) {
        this.selectBackgroundColor = selectBackgroundColor;
    }


    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public ColorsText(Context context) {
        super(context);
        init();
    }

    public ColorsText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 设置光标颜色
    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    private void init() {
        mNumberPadding = DpiUtils.dip2px(getContext(), 5);
        mTextPadding = DpiUtils.dip2px(getContext(), 4);
        mLineNumberBgStrokeWidth = DpiUtils.dip2px(getContext(), 2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setGravity(Gravity.START);
        // 设值背景透明
        setBackgroundColor(Color.TRANSPARENT);
        // 设值字体大小
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        // 设置字体颜色(透明是为了兼容不能反射绘制光标以及选择文字背景的情况)
        setTextColor(Color.TRANSPARENT);
        setTypeface(Typeface.MONOSPACE);
        setPadding(0, DpiUtils.dip2px(getContext(), 2), DpiUtils.dip2px(getContext(), 8), DpiUtils.dip2px(getContext(), 48));

        //setMovementMethod(null);

    }


    /**
     * 生成每行文字对应的行号，如果行首为换行符则需要显示行号
     *
     * @return
     */
    public Map<Integer, String> getLineNumbers() {

        if (this.lineNumMap != null && getLineCount() == mlineCountCache) {
            return this.lineNumMap;
        } else {
            mlineCountCache = lineCountCache;
            Map<Integer, String> maps = new HashMap<>();
            Layout layout = getLayout();
            if (layout == null) {
                return maps;
            }
            int lineNumber = 1;
            maps.put(0, Integer.toString(lineNumber));
            int lineCount = getLineCount();
            mMaxLineNumber = 1;
            for (int i = 1; i < lineCount; i++) {
                int charPos = layout.getLineStart(i);
                if (getText().charAt(charPos - 1) == '\n') {
                    lineNumber++;
                    maps.put(i, Integer.toString(lineNumber));
                    mMaxLineNumber = lineNumber;
                }
            }
            this.lineNumMap = maps;
            return maps;
        }
    }


    /**
     * 获取可视范围的文字首行与尾行
     *
     * @param rect
     * @param ret
     */
    public void getLineRangeForDraw(Rect rect, int[] ret) {
        if (rangDraw) {
            ret[0]=this.lastLineRang[0];
            ret[1]=this.lastLineRang[1];
        } else {
            Layout layout = getLayout();
            final int top = Math.max(rect.top, 0);
            final int bottom = Math.min(layout.getLineTop(layout.getLineCount()), rect.bottom);
            if (top >= bottom) {
                ret[0] = -1;
                ret[1] = -1;
                return;
            }
            ret[0] = layout.getLineForVertical(top);
            ret[1] = layout.getLineForVertical(bottom);
            
            lastLineRang=ret;
        }
    }


    /**
     * 计算可视范围文字的首字位置
     *
     * @param widths
     * @return [位置，偏移]
     */
    public float[] getLineFirstCharPosForDraw(float[] widths) {
        float max = (getViewScrollX() - getPaddingLeft());
        int count = 0;
        float size = 0;
        for (int i = 0; i < widths.length; i++) {

            if (size + widths[i] >= max) {
                break;
            }
            size += widths[i];
            count++;
        }
        return new float[]{count, size};
    }


    Method getUpdatedHighlightPathMthod;

    /**
     * 获取绘制选择文字背景的Path路径
     *
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Path getUpdatedHighlightPath() throws NoSuchMethodException,
    InvocationTargetException, IllegalAccessException {
        if (getUpdatedHighlightPathMthod == null) {
            getUpdatedHighlightPathMthod = TextView.class
                .getDeclaredMethod("getUpdatedHighlightPath");
            getUpdatedHighlightPathMthod.setAccessible(true);
        }
        return (Path) getUpdatedHighlightPathMthod.invoke(this);
    }


    Field mHighlightPaintField;

    /**
     * 获取绘制选择文字背景的Paint画笔
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    Paint getHighlightPaint() throws IllegalAccessException,
    NoSuchFieldException {
        if (mHighlightPaintField == null) {
            mHighlightPaintField = TextView.class
                .getDeclaredField("mHighlightPaint");
            mHighlightPaintField.setAccessible(true);
        }
        return (Paint) mHighlightPaintField.get(this);
    }

    Field mEditorField;

    /**
     * 获取用于编辑的Editor
     *
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    Object getEditor() throws NoSuchFieldException, IllegalAccessException {
        if (mEditorField == null) {
            mEditorField = TextView.class.getDeclaredField("mEditor");
            mEditorField.setAccessible(true);
        }
        return mEditorField.get(this);
    }


    /**
     * 绘制光标以及文字选择背景
     *
     * @param canvas
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    void drawCursorAndSelectPath(Canvas canvas) throws NoSuchMethodException,
    IllegalAccessException, InvocationTargetException,
    NoSuchFieldException {

        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        Path highlight = getUpdatedHighlightPath();
        Paint mHighlightPaint = getHighlightPaint();
        //设置选择文字背景颜色
        if (selectBackgroundColor != 0) {
            mHighlightPaint.setColor(selectBackgroundColor);
        }
        canvas.save();

        //getCompoundPaddingLeft获取真正的左内边距，getExtendedPaddingTop获取真正的上外边距
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        try {

            if (highlight != null) {
                if (selectionEnd == selectionStart) {
                    //绘制光标
                    Paint paint = getPaint();
                    paint.setColor(cursorColor);
                    canvas.drawRect(getCursorRect(), paint);
                } else {
                    //绘制选择文字阴影
                    canvas.drawPath(highlight, mHighlightPaint);
                }
            }

        } finally {
            canvas.restore();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = getPaint();
        //这里再设置，防止卡
        /*
         if (paint.getTextSize() != textSize) {
         paint.setTextSize(textSize);
         requestLayout();
         }
         */


        try {
            // 优化速度绘制光标以及选择文字背景
            drawCursorAndSelectPath(canvas);
        } catch (Exception e) {
            // 反射调用失败，使用默认的方法绘制背景，会绘制文字

            super.onDraw(canvas);
        }

        // 画文字
        canvas.save();
        canvas.translate(0, getExtendedPaddingTop());

        synchronized (colorLock) {
            drawText(canvas);
        }

        canvas.restore();
        // 绘制分割线

        paint.setStrokeWidth(lineNumberSplitWidth);
        paint.setColor(lineNumberSplitColor);
        canvas.drawLine(getPaddingLeft() - mTextPadding, 0, getPaddingLeft() - mTextPadding, getHeight(), paint);

        // 根据行号计算左边距padding
        String max = Integer.toString(mMaxLineNumber);
        float lineNumberSize = getPaint().measureText(max) + mNumberPadding + mNumberPadding + mTextPadding;


        if (getPaddingLeft() != lineNumberSize) {
            setPadding((int) lineNumberSize, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            invalidate();
        }
    }


    // 获取选择行，如果多余一行，返回-1
    public int getSelectLine() {
        Layout layout = getLayout();
        if (getSelectionStart() != getSelectionEnd()) {
            return -1;
        }
        return layout.getLineForOffset(getSelectionStart());
    }

    // 绘制文本着色

    private void drawText(Canvas canvas) {

        Map<Integer, String> lineNumbles = getLineNumbers();

        Layout layout = getLayout();
        int selectLine = getSelectLine();
        int range[] = new int[4];
        {// 计算需要绘制的行号所需要的范围

            int clipLeft = 0;
            int clipTop = (getScrollView().getScrollY() == 0) ? 0
                : getExtendedPaddingTop() + getScrollView().getScrollY()
                - getScrollView().getPaddingTop();
            int clipRight = getWidth();
            int clipBottom = clipTop + getScrollView().getHeight();
            Rect rect = new Rect(clipLeft, clipTop, clipRight, clipBottom);
            getLineRangeForDraw(rect, range);
        }
        int firstLine = range[0];
        int lastLine = range[1];

        if (rangDraw) {
            firstLine = Math.max(0, firstLine - (lastLine - firstLine));
            lastLine += (lastLine - firstLine);
        }

        this.visualFirstLine = firstLine;
        this.visualLastLine = lastLine;

        if (firstLine < 0) {
            return;
        }
        int previousLineBottom = layout.getLineTop(firstLine);
        int previousLineEnd = layout.getLineStart(firstLine);
        int lineCount = getLineCount();
        Paint paint = getPaint();
        //paint.setTextSize(textSize);


        if (delay.IsExceed()) {
            //语法检查
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            check.publicTest(getText().toString());
                        } catch (Exception e) {}
                        errorShowSafe = check.getError();

                    }
                }).start();
            //层数
            if (check instanceof GrammarCheck.JSON) {
                new Thread(new Runnable(){


                        @Override
                        public void run() {
                            try {
                                jsonPath = ((GrammarCheck.JSON)check).getSurface(getText().subSequence(0, getSelectionStart()));
                                LOG.print(jsonPath, "");
                            } catch (Exception e) {}


                        }
                    }).start();
            }
        }


        check.prepare(false);


        //mPaint.setFlags(Paint.UNDERLINE_TEXT_FLAG);


        for (int lineNum = firstLine; lineNum <= lastLine
             && lineNum < lineCount; lineNum++) {
            int start = previousLineEnd;

            previousLineEnd = layout.getLineStart(lineNum + 1);

            int end = layout.getLineVisibleEnd(lineNum);
            int ltop = previousLineBottom;
            int lbottom = layout.getLineTop(lineNum + 1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - layout.getLineDescent(lineNum);
            int left = getPaddingLeft();
            // 绘制选择行背景
            if (lineNum == selectLine) {
                paint.setColor(lineNumberBackgroundColor);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mLineNumberBgStrokeWidth);
                canvas.drawRect(getPaddingLeft() - mLineNumberBgStrokeWidth, ltop, getRight() - getPaddingRight() + mLineNumberBgStrokeWidth, lbottom, paint);
                paint.setStyle(Paint.Style.FILL);
            }
            // 绘制行号
            String lineNumberText = lineNumbles.get(lineNum);
            if (lineNumberText != null) {
                paint.setColor(lineNumberColor);
                canvas.drawText(lineNumberText, 0, lineNumberText.length(), mNumberPadding,
                                lbaseline, paint);
            }
            int textLength = getText().length();



            // 绘制文字
            if (start < textLength) {
                //计算需要绘制的文字位置
                //获取改行所有文字宽度
                float[] widths = new float[end - start + 1];
                paint.getTextWidths(getText(), start, end, widths);
                //计算获取看到的文字第一个位置，和对应的偏移x
                float firstNeedDrawPos[] = getLineFirstCharPosForDraw(widths);
                int firstPos = (int) firstNeedDrawPos[0];
                float offsetX = firstNeedDrawPos[1];
                int maxOffX = getViewScrollX() + getVisibleWidth();
                //移动行数
                check.nextLine(left + offsetX, lbaseline);

                // 文字着色
                for (int i = start + firstPos; i < end && i < textLength;) {
                    if (offsetX > maxOffX) {
                        break;
                    }
                    int color = getCodeColor(i);
                    {
                        float fontWidths = widths[i - start];
                        int fontCount = 1;
                        for (int j = i + 1; j < end && j < textLength; j++) {
                            if (color == getCodeColor(j)) {
                                fontCount++;
                                fontWidths += widths[j - start];
                            } else {
                                break;
                            }
                        }
                        if (color == 0) {
                            color = defaultTextColor;
                        }

                        paint.setColor(color);


                        check.setPaint(getText().subSequence(i, i + fontCount).toString(), fontWidths, paint);
                        errorShow = errorShowSafe;

                        if (errorShow != null) {

                            if (i <= errorShow.point && errorShow.point <= i + fontCount) {
                                paint.setUnderlineText(true);
                                paint.setColor(Color.RED);
                            } else {
                                paint.setUnderlineText(false);
                            }
                        } else {
                            paint.setUnderlineText(false);
                        }
                        canvas.drawText(getText(), i, i + fontCount, left + offsetX, lbaseline, paint);
                        //语法处增加内容

                        i += fontCount;
                        offsetX += fontWidths;

                    }
                }
            }
        }
        /*
         check.getPath().moveTo(0,0);
         check.getPath().lineTo(100,100);
         */

        //for结束，绘制内容

    }

    private int getCodeColor(int i) {
        if (codeColors != null && i < codeColors.length) {
            return codeColors[i];
        }
        return 0;
    }

    public int[] getCodeColors() {
        int textLength = getText().length();
        if (codeColors == null) {
            codeColors = new int[textLength];
        }
        //如果文字长度小于颜色长度，生成新的颜色数组
        if (textLength >= codeColors.length) {
            int newColors[] = new int[textLength + 500];
            for (int i = 0; i < codeColors.length; i++) {
                newColors[i] = codeColors[i];
            }
            codeColors = newColors;
        }
        return codeColors;
    }

    public int getViewScrollX() {
        return getScrollView().getScrollX();
    }

    public int getViewScrollY() {
        return getScrollView().getScrollY();
    }

    public View getScrollView() {
        return scrollView == null ? this : scrollView;
    }

    public void setScrollView(View scrollView) {
        this.scrollView = scrollView;
    }

    public int getVisibleWidth() {
        return Math.min(getWidth(), getScrollView().getWidth());
    }

    public int getVisibleHeight() {
        return Math.min(getHeight(), getScrollView().getHeight());
    }


    public Rect getCursorRect() {
        Layout layout = getLayout();
        final int offset = getSelectionStart();
        final int line = layout.getLineForOffset(offset);
        final int top = layout.getLineTop(line);
        final int bottom = layout.getLineTop(line + 1);
        float horizontal = layout.getSecondaryHorizontal(offset);
//        horizontal = Math.max(0.5f, horizontal - 0.5f);
        int left = (int) (horizontal);
        return new Rect(left, top, left + DpiUtils.dip2px(getContext(), 1), bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:

                if (event.getY() > getHeight() - getPaddingBottom()) {
                    int len = getText().length();
                    if (len > 0 && getText().charAt(len - 1) != '\n') {

                    }
                    append("\n");

                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setTextSize(float size) {
        //this.textSize = size;
        super.setTextSize(size);
    }

    @Override
    public float getTextSize() {
        return super.getTextSize();
    }

    //原EditText不合理的onMeasure在这里重新写
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (fixedWidth <= 0 && fixedHeight <= 0) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            fixedWidth = getMeasuredWidth();
            fixedHeight = getMeasuredHeight();

            visualFirstLine = 0;
            visualLastLine = getLineCount();
        } else {
            int specSizeH = getParentWidth();
            int specSizeW = getParentHeight();

            fixedHeight = (getLineCount() + 1) * getLineHeight()+getMinHeight();
            fixedWidth = getCompoundPaddingLeft() + getCompoundPaddingRight() + getLineMaxWidth(getLayout(), visualFirstLine, visualLastLine);
            fixedWidth = Math.max(fixedWidth, specSizeW);
            fixedHeight = Math.max(fixedHeight, specSizeH);
            setMeasuredDimension(fixedWidth, fixedHeight);

        }


    }

    protected int getParentWidth() {
        return 0;
    }

    protected int getParentHeight() {
        return 0;
    }
    private int getLineMaxWidth(Layout layout, int startLine, int endLine) {
        if (layout == null)return 2000;
        int n = endLine;

        float max = 0;
        try {
            for (int i = startLine; i < n; i++) {
                max = Math.max(max, layout.getLineWidth(i));
            }
        } catch (Exception e) {
            return 0;
        }
        return (int) Math.ceil(max);
    }
    @Override
    public int getLineCount() {

        if (super.getLayout() != null) {
            this.lineCountCache = super.getLineCount();
        }

        return lineCountCache;
    }


}
