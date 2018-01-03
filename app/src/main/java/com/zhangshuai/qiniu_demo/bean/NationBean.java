package com.zhangshuai.qiniu_demo.bean;

import com.alipay.android.phone.mrpc.core.gwprotocol.Serializer;
import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangshuai on 2017/12/1.
 */

public class NationBean implements Serializable {

    public ArrayList<Nation> data;

    public class Nation implements IPickerViewData{
        public int id;
        public String name;
        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }

}
