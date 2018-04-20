package com.bigdata101.bigdata101;

import com.bigdata101.bigdata101.service.DateFormatter;

import org.junit.Test;
import java.util.Calendar;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * Created by Silas on 4/18/2018.
 */

public class DateFormatterTest {

    @Test
    public void dateFormatter(){
        long jsDate =  1524082680079L;
        assertEquals(DateFormatter.getFormattedDate(jsDate), ("18-04-2018"));

    }
}
