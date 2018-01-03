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
import android.widget.TextView;


import com.gxz.library.StickyNavLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main3Activity extends AppCompatActivity {
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.bt_add)
    Button bt_add;
    @BindView(R.id.mStickyNavLayout)
    StickyNavLayout mStickyNavLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        MyRvAdapter myRvAdapter = new MyRvAdapter();
        mRecyclerview.setAdapter(myRvAdapter);
        add();
    }

    private int index = 20;

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
        int childCount = ll.getChildCount();
        Log.i("_zs_", "add: childCount = " + childCount);
    }
}
