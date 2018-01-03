package com.zhangshuai.qiniu_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhangshuai.qiniu_demo.customview.StickyNavLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.bt_add)
    Button bt_add;
    @BindView(R.id.mStickyNavLayout)
    StickyNavLayout mStickyNavLayout;
    @BindView(R.id.mRecyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.mRecyclerview_top)
    RecyclerView mRecyclerview_top;
//    @BindView(R.id.scrollView)
//    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        mStickyNavLayout.setMaxTopTranslationYRate(0);//设置相对滚动速率，0表示不相对滚动
        mStickyNavLayout.setOnScollListener(new StickyNavLayout.OnScollListener() {
            @Override
            public void onScroll(float positionOffset, int positionOffsetPixels, int offsetRange) {

            }

            @Override
            public void onStateChanged(int state) {

            }
        });
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview_top.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview_top.setLayoutManager(linearLayoutManager2);
        MyRvAdapter myRvAdapter = new MyRvAdapter();
        mRecyclerview.setAdapter(myRvAdapter);
        MyRvAdapter myRvAdapter2 = new MyRvAdapter();
        mRecyclerview_top.setAdapter(myRvAdapter2);
        mRecyclerview_top.requestDisallowInterceptTouchEvent(true);
        mRecyclerview_top.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("_zs_", "onScrolled: dy = "+dy);
            }
        });
        add();

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("_zs_", "onScrolled: 是否能向上滚动"+recyclerView.canScrollVertically(1));
                Log.i("_zs_", "onScrolled: 是否能向下滚动"+recyclerView.canScrollVertically(-1));
            }
        });

    }


    int index = 20;

    @OnClick(R.id.bt_add)
    void add() {

        ll.removeAllViews();
        index += 2;
        for (int i = 0; i <index ; i++) {
            TextView textView = new TextView(this);
            textView.setText("hahhha " + i);

            textView.setTextSize(28);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            ll.addView(textView);
        }
        mStickyNavLayout.notifyLayout();


//        mStickyNavLayout.invalidate();
        int childCount = ll.getChildCount();
        Log.i("_zs_", "add: childCount = " + childCount);
    }
}
