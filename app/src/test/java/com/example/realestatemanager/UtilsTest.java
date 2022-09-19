package com.example.realestatemanager;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

import com.example.realestatemanager.utils.Utils;

public class UtilsTest {


    @Test
    public void convertEuroToDollarTest() {
        assertEquals(1111, Utils.convertEuroToDollar(1000));
    }

    @Test
    public void convertDollarToEuroTest() {
        assertEquals(812,Utils.convertDollarToEuro(1000));
    }

    @Test
    public void getTodayDateTest() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String actualDate = Utils.getTodayDate();
        assertEquals(dateFormat.format(new Date()), actualDate);
    }

}