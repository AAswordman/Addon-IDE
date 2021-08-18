/*
 * Copyright 2018. who<980008027@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsdroid.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
<<<<<<< HEAD
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import chineseframe.屏幕工具;
import bms.helper.tools.TimeDelayer;
=======
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.graphics.Matrix;
import chineseframe.屏幕工具;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68


/**
 * Created by Administrator on 2018/2/11.
 */

public class CodePane extends HVScrollView 
implements OnTouchListener,OnScaleGestureListener {
    public static final float SCALE_MAX = 40.0f;
    private static final float SCALE_MID = 20f;

    /**
     * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
     */
    private float initScale = 5f;
<<<<<<< HEAD

    private float beginScale;

    private int[] codeLocation;
=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        
<<<<<<< HEAD
        float endScale=beginScale;
        
=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            /**
             * 最大值最小值判断
             */
            if (scaleFactor * scale < initScale) {
                scaleFactor = initScale / scale;
            }
            if (scaleFactor * scale > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale;
            }
            /**
             * 设置缩放比例
             */
<<<<<<< HEAD
            mScaleMatrix *= scaleFactor;
            endScale=beginScale*scaleFactor;
        }
        
        
        float x=(detector.getFocusX()-codeLocation[0])/mCodeText.getWidth();
        float y=(detector.getFocusY()-codeLocation[1])/mCodeText.getHeight();
        
        mCodeText.getLocationInWindow(codeLocation); //获取在当前窗口内的绝对坐标
        
        
        Animation animation = new ScaleAnimation(
            beginScale, endScale, beginScale, endScale, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,0 );
        animation.setDuration(detector.getTimeDelta());
        mCodeText.clearAnimation();
        mCodeText.startAnimation(animation);
        
        
        setScrollX((int)((getScrollX())*endScale/beginScale));
        setScrollY((int)((getScrollY())*endScale/beginScale));
        
        beginScale=endScale;
=======
            mScaleMatrix*=scaleFactor;
            setScale();
        }
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        return true;
    }

    private void setScale() {
        mCodeText.setTextSize(mScaleMatrix);
<<<<<<< HEAD
        
=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector p1) {
<<<<<<< HEAD
        delayer.UpdateLastTime();
        mCodeText.AddRangDraw();
        this.beginScale=1;
        codeLocation = new int[2] ;
        mCodeText.getLocationInWindow(codeLocation); //获取在当前窗口内的绝对坐标
        
=======
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector p1) {
<<<<<<< HEAD
        setScale();
        mCodeText.RemoveRangDraw();
        
        /*
        scrollTo((int)(codeLocation[0]-p1.getFocusX()*beginScale+p1.getFocusX()),
        (int)(codeLocation[1]-p1.getFocusY()*beginScale+p1.getFocusY()));
        */
        
=======
       
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
    }
    /**
     * 获得当前的缩放比例
     * @return
     */
    public final float getScale() {
<<<<<<< HEAD
=======
        
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        return mScaleMatrix;
    }

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleMatrix = SCALE_MID;
<<<<<<< HEAD

    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
        mScaleGestureDetector.onTouchEvent(p2);
        return false;
    }

    CodeText mCodeText;
    int mCodeTextMinHeight;
    int mCodeTextMinWidth;
    TimeDelayer delayer;
=======
    
    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
       
        mScaleGestureDetector.onTouchEvent(p2);
        return false;
    }
    
    CodeText mCodeText;
    int mCodeTextMinHeight;
    int mCodeTextMinWidth;
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68

    public CodePane(Context context) {
        super(context);
        init();

    }

    public CodePane(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置背景颜色
        setBackgroundColor(0XFF333333);
        mCodeText = new CodeText(getContext());
        mCodeText.setScrollView(this);
        addView(mCodeText);
<<<<<<< HEAD
        delayer=new TimeDelayer(0);
=======
        
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
        //设置缩放
        this.setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        setFlexible(false);
        setScale();
        mCodeText.setPWidth(屏幕工具.获取屏幕宽度(getContext()));
        mCodeText.setPHeight(屏幕工具.获取屏幕高度(getContext()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //计算CodeText宽高
        int codeWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int codeHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (mCodeTextMinHeight != codeHeight || mCodeTextMinWidth != codeWidth) {
            mCodeTextMinWidth = codeWidth;
            mCodeTextMinHeight = codeHeight;
            mCodeText.setMinWidth(mCodeTextMinWidth);
            mCodeText.setMinHeight(mCodeTextMinHeight);
            invalidate();
            return;
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //滑动的时候，通知CodeText绘制高亮
        mCodeText.postInvalidate();
    }

    public CodeText getCodeText() {
        return mCodeText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //this是activity对象
<<<<<<< HEAD


    }

=======
        
        
    }
    
>>>>>>> aa41e3931568d839484ad45420af53d27f2b4d68
}
