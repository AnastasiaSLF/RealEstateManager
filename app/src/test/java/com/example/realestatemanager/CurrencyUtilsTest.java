package com.example.realestatemanager;


import org.junit.Test;

import static org.junit.Assert.*;

import com.example.realestatemanager.utils.CurrencyUtils;

public class CurrencyUtilsTest {

    @Test
    public void convertDoubleToCurrency() {
        assertEquals(CurrencyUtils.convertDoubleToCurrency(2000), "$2,000");
    }

    @Test
    public void calculateLoan() {
        assertEquals(Math.rint(CurrencyUtils.calculateMonthlyRepayment(150_000, 0.001159, 20)), 626, 0);
    }

    @Test
    public void annualRateToMonthlyRate() {
        assertEquals(CurrencyUtils.annualRateToMonthlyRate(0.014), 0.001159, 0.001);
    }
}
