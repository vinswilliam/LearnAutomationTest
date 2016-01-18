package com.siuli.andr.whitebird.utilities;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by william on 1/13/2016.
 */
public class Util {

    /**
     * parsing string to calendar with timezone
     * @param strDate
     * @param defaultFormat
     * @param newFormatDate
     * @return formated string
     */
    public static String parsingStringDateTime(String strDate,String defaultFormat, String newFormatDate){

        String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ssZ";

        if(TextUtils.isEmpty(defaultFormat)){
            defaultFormat = FORMAT_DATETIME;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(defaultFormat);
        SimpleDateFormat sdfCustom = new SimpleDateFormat(newFormatDate);
        Date date;
        String returnStr = "";
        try {
            date = sdf.parse(strDate);
            returnStr = sdfCustom.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    public static String parsingLongDateTime(long calInMillis, String newFormatDate){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(calInMillis);

        SimpleDateFormat sdfCustom = new SimpleDateFormat(newFormatDate);
        return sdfCustom.format(cal.getTime());
    }
}
