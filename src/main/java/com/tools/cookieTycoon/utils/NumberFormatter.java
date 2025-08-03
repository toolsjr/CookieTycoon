package com.tools.cookieTycoon.utils;

import java.text.DecimalFormat;

public class NumberFormatter {
    private static final String[] SUFFIXES = new String[]{"", "K", "M", "B", "T", "Q", "Qt", "Sx", "Sp", "Oc", "Non", "Dec", "Und", "Dod", "Trd", "Qtd"};
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.##");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.##%");

    public static String format(double number) {
        if (number < 1000000) {
            return FORMATTER.format(number);
        }

        int exp = (int) (Math.log(number) / Math.log(1000));
        if (exp >= SUFFIXES.length) {
            exp = SUFFIXES.length - 1;
        }

        return FORMATTER.format(number / Math.pow(1000, exp)) + SUFFIXES[exp];
    }

    public static String formatPercent(double decimalValue) {
        return PERCENT_FORMAT.format(decimalValue);
    }
}
