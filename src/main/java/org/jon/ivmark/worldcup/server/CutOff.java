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
}
