package org.javaswift.joss.util;

import java.util.Date;

public class LocalTime {

    public static Date currentDate() {
        return new Date();
    }

    public static long currentTime() {
        return currentDate().getTime();
    }

}
