package org.jon.ivmark.worldcup.server;

import com.google.appengine.repackaged.org.joda.time.DateTime;

public class CutOff {
    private static final DateTime CUT_OFF = DateTime.parse("2014-06-12T21:59:00.000+02:00");

    public static boolean isBeforeCutOff(DateTime dateTime) {
        return dateTime.isBefore(CUT_OFF);
    }
}
