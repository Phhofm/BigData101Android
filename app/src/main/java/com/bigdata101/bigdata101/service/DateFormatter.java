package com.bigdata101.bigdata101.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Silas on 4/18/2018.
 */

public class DateFormatter {

    public static String getFormattedDate(long dateInMilliseconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMilliseconds);

        Date date = calendar.getTime();

        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        return formattedDate;
    }
}
