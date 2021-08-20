package bms.helper.android.v4;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.ArrayList;
import android.view.LayoutInflater;

public class ViewPagerHelper {
    PagerAdapter mPagerAdapter;
    ViewPager viewPager;
    public void run(){
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        
    }
    public ViewPagerHelper(ViewPager viewPager){
        this.viewPager=viewPager;
        mPagerAdapter=new PagerAdapter(){
            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
    }
    public ViewPagerHelper(ViewPager viewPager,final String[] mTitles){
        this.viewPager=viewPager;
        mPagerAdapter=new PagerAdapter(){
            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
    }
    private ArrayList<View> pageview=new ArrayList<>();
    public ArrayList<View> getViewList(){
        return pageview;
    }
    public PagerAdapter getAdapter(){
        return mPagerAdapter;
    }
    public void add(View view){
        pageview.add(view);
    }
    /*
    public View add(int i){
        LayoutInflater inflater=getLayoutInflater();
        this.add(inflater.inflate(R.layout.res_download_download, null));
    }
    */
    public View get(int i){
        return pageview.get(i);
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
	}
    
}
