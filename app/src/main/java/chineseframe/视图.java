package chineseframe;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import android.view.ViewGroup;
import android.view.LayoutInflater;

public abstract class 视图 extends View
{
	public int 帧数=1000;
	public boolean 线程开关=true;
	public 画笔 画笔;
	public 视图(活动 活动)
	{
		
		super(活动);
		init();
	}
	public 视图(活动 活动, AttributeSet set)
	{
		super(活动, set);
		init();
	}
	
	public 视图(活动 活动, AttributeSet set, int id)
	{
		super(活动, set, id);
		init();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width = 0;
		int height = 0 ;
		if (widthMode == MeasureSpec.EXACTLY)
		{
			width = widthSize;
		} 
		if (heightMode == MeasureSpec.EXACTLY)
		{
			height = heightSize;
		} 
		setMeasuredDimension(width, height);
	}
	
	private void init()
	{
		画笔 = new 画笔(new Paint());
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					while (线程开关)
					{
						绘图线程();
						postInvalidate();
						try
						{
							Thread.sleep(帧数);
						}
						catch (InterruptedException e)
						{
							Log.e("thread", e.toString());
						}
					}
				}
			}).start();
	}

	public 画笔 获取画笔()
	{
		return this.画笔;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		绘制界面(new 画纸(canvas));
	}
	public void 设置点击事件(点击事件 点击事件){
		setOnClickListener(点击事件);
	}
	public abstract void 绘制界面(画纸 画纸)
	public abstract void 绘图线程()
	
	public void 重绘()
	{
		invalidate();
	}
	public void 线程重绘()
	{
		postInvalidate();
	}
	public void 设置宽度(View view, int 宽) {
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = 宽;
    }
	public void 设置长度(View view, int 长) {
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = 长;
    }
	public static 视图 加载布局(Context 活动,int 布局id){
		LayoutInflater layout=LayoutInflater.from(活动);
		return (视图)layout.inflate(布局id,null);
	}
	public int 宽(){
		ViewGroup.LayoutParams params = this.getLayoutParams();
      return  params.height ;
	}
	public int 长(){
		ViewGroup.LayoutParams params = this.getLayoutParams();
       return params.width;
	}
}
