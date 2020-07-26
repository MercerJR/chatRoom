package com.train.chat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author MercerJR
 * @Data 2020/7/26 10:35
 */
public class DateFormatUtil {

    public static String getDateByMiles(Long miles){
        Long time = miles ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }
}
