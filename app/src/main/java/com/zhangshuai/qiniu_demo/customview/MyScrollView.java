package com.zhangshuai.qiniu_demo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by zhangshuai on 2017/12/7.
 */

public class MyScrollView extends ScrollView {

    private float mLastMotionY;
    private float mLastMotionX;
    private float mFirstMotionY;
    private float mFirstMotionX;
    private int mTouchSlop;

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mLastMotionX = event.getX();
                mFirstMotionY = event.getY();
                mFirstMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                float y = event.getY();
                float  x = event.getX();

                float dy = y - mFirstMotionY;
                float dx = x - mFirstMotionX;
                Log.i("_zs_", "dispatchTouchEvent:Math.abs(dy) ="+Math.abs(dy));
                Log.i("_zs_", "dispatchTouchEvent:mTouchSlop= "+mTouchSlop);
                if (Math.abs(dy) > mTouchSlop) {
                    if (Math.abs(dy) > Math.abs(dx)) {
                        mLastMotionY = y;
                        mLastMotionX = x;
                        View childView = getChildAt(0);
                        Log.i("_zs_", "dispatchTouchEvent: getScrollY()="+getScrollY());
                        Log.i("_zs_", "dispatchTouchEvent: childView.getHeight()="+childView.getHeight());
                        Log.i("_zs_", "dispatchTouchEvent: getHeight()="+getHeight());
//                        Log.i("_zs_", "dispatchTouchEvent: super.dispatchTouchEvent(event)="+super.dispatchTouchEvent(event));
                        /*if(getScrollY() == (childView.getHeight()-getHeight()-500)){
                            //滑动到底部
                            //TODO
                            Log.i("_zs_", "dispatchTouchEvent: 滑动到底部");
                            return false;
                        }*/
                        if (getScrollY() > 500){
                            return false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return super.dispatchTouchEvent(event);
    }
}
