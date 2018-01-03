package com.zhangshuai.qiniu_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhangshuai.qiniu_demo.customview.FlowLayout;
import com.zhangshuai.qiniu_demo.customview.MultiColumnFolwView;

public class CustomActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;
    private MultiColumnFolwView mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        mFlowLayout = findViewById(R.id.fl);
        mf = findViewById(R.id.mf);

        for (int i = 0; i <20 ; i++) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.item_view, null, false);
            mFlowLayout.addView(inflate);
        }

        for (int i = 0; i <20 ; i++) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.multi_item_view, null, false);
            TextView tv = inflate.findViewById(R.id.tv);
            tv.setText("张帅 +==+ "+i);
            mf.addView(inflate);
        }

    }
}
