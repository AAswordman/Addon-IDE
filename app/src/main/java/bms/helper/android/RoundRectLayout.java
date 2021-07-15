package bms.helper.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RoundRectLayout extends RelativeLayout {

    private Path mPath;
    private int mRadius;

    private int mWidth;
    private int mHeight;
    private int mLastRadius;

    public static final int MODE_NONE = 0;
    public static final int MODE_ALL = 1;
    public static final int MODE_LEFT = 2;
    public static final int MODE_TOP = 3;
    public static final int MODE_RIGHT = 4;
    public static final int MODE_BOTTOM = 5;

    private int mRoundMode = MODE_ALL;

    public RoundRectLayout(Context context) {
        super(context);
        init();
    }

    public RoundRectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setBackgroundDrawable(new ColorDrawable(0x33ff0000));
        mPath = new Path();
        mPath.setFillType(Path.FillType.EVEN_ODD);
        setCornerRadius(15);
    }

    /**
     * 设置是否圆角裁边
     * @param roundMode
     */
    public void setRoundMode(int roundMode){
        mRoundMode = roundMode;
    }

    /**
     * 设置圆角半径
     * @param radius
     */
    public void setCornerRadius(int radius){
        mRadius = radius;
    }

    private void checkPathChanged(){
        if(getWidth() == mWidth && getHeight() == mHeight && mLastRadius == mRadius){
            return;
        }
        mWidth = getWidth();
        mHeight = getHeight();
        mLastRadius = mRadius;
        mPath.reset();
        switch (mRoundMode){
            case MODE_ALL:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight), mRadius, mRadius, Path.Direction.CW);
                break;
            case MODE_LEFT:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
								   new float[]{mRadius, mRadius, 0, 0, 0, 0, mRadius, mRadius},
								   Path.Direction.CW);
                break;
            case MODE_TOP:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
								   new float[]{mRadius, mRadius, mRadius, mRadius, 0, 0, 0, 0},
								   Path.Direction.CW);
                break;
            case MODE_RIGHT:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
								   new float[]{0, 0, mRadius, mRadius, mRadius, mRadius, 0, 0},
								   Path.Direction.CW);
                break;
            case MODE_BOTTOM:
                mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
								   new float[]{0, 0, 0, 0, mRadius, mRadius, mRadius, mRadius},
								   Path.Direction.CW);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(mRoundMode != MODE_NONE){
            int saveCount = canvas.save();
            checkPathChanged();
            canvas.clipPath(mPath);
            super.draw(canvas);
            canvas.restoreToCount(saveCount);
        }else {
            super.draw(canvas);
        }
    }
}
