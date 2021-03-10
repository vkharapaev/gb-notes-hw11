package com.headmostlab.notes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Utils {
    public static Date parseData(String stringDate) {
        try {
            return DateFormat.getDateInstance().parse(stringDate);
        } catch (ParseException ignore) {
            return new Date();
        }
    }
}
