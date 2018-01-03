package com.zhangshuai.qiniu_demo.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zhangshuai on 2017/12/7.
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        switch (e.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.i("MyRecyclerView", "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("MyRecyclerView", "onTouchEvent: ACTION_MOVE");

                float x = e.getX();
                float y = e.getY();
                if (y>x){
                    LayoutManager layoutManager = getLayoutManager();
//                    requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.i("MyRecyclerView", "onTouchEvent: ACTION_UP");
                break;
        }

        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }
}
