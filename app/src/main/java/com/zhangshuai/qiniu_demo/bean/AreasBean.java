package com.zhangshuai.qiniu_demo.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangshuai on 2017/12/1.
 */

public class AreasBean implements Serializable{

    public ArrayList<CountryBean> nations;

    public class CountryBean implements IPickerViewData{
        public int code;
        public String name;
        public ArrayList<ProvineBean> provines;

        @Override
        public String getPickerViewText() {
            return name;
        }

        public class ProvineBean implements IPickerViewData{
            public int code;
            public String name;
            public ArrayList<CityBean> cities;

            @Override
            public String getPickerViewText() {
                return name;
            }

            public class CityBean implements IPickerViewData{
                public int code;
                public String name;

                @Override
                public String getPickerViewText() {
                    return name;
                }
            }
        }

    }
}
