package com.zhangshuai.qiniu_demo.bean;

/**
 * Created by zhangshuai on 2017/12/3.
 */

public class AnswerBean extends Bean {
    public int answerId;
    public String answer;
    public boolean isAbsCondition;

    public AnswerBean(int answerId, String answer, boolean isAbsCondition) {
        this.answerId = answerId;
        this.answer = answer;
        this.isAbsCondition = isAbsCondition;
        type = 1;
    }
}
