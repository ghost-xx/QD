package com.ghostx.qd.utils;


import java.util.Random;

public class MobileInfoUtil {

    public static String getUUID() {
        String temp = "0000000" + new Random(System.currentTimeMillis()).nextInt(9999999);
        String uuidStr = 86516602 + temp.substring(temp.length()-7);
        return uuidStr;
    }

}
