package com.train.chat.utils;

import org.springframework.stereotype.Component;

/**
 * @Author MercerJR
 * @Data 2020/7/15 9:51
 */
@Component
public class IdUtil {

    public String getPrimaryKey() {
        //随机7位数
        return MathUtil.makeUpNewData(Thread.currentThread().hashCode() + "", 3)
                + MathUtil.randomDigitNumber(7);
    }
}