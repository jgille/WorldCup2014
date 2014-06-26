package org.jon.ivmark.worldcup.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CutOff {

    public static boolean isBeforeCutOff() {
        try {
            long cutOff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-06-12 21:59:59+0200").getTime();
            return System.currentTimeMillis() < cutOff;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isBeforeEightsFinalCutOff() {
        try {
            long cutOff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-06-28 17:59:59+0200").getTime();
            return System.currentTimeMillis() < cutOff;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWithinQuarterFinalPeriod() {
        try {
            long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-06-28 17:59:59+0200").getTime();
            long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-04 17:59:59+0200").getTime();
            long time = System.currentTimeMillis();
            return time > start && time < end;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWithinSemiFinalPeriod() {
        try {
            long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-04 17:59:59+0200").getTime();
            long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-08 21:59:59+0200").getTime();

            long time = System.currentTimeMillis();
            return time > start && time < end;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWithinBronzeMatchPeriod() {
        try {
            long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-08 21:59:59+0200").getTime();
            long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-12 21:59:59+0200").getTime();

            long time = System.currentTimeMillis();
            return time > start && time < end;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWithinFinalPeriod() {
        try {
            long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-08 21:59:59+0200").getTime();
            long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
                    .parse("2014-07-13 20:59:59+0200").getTime();

            long time = System.currentTimeMillis();
            return time > start && time < end;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
