package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {

    public static void main(String[] args){
        test1();
    }

    private static void test1() {
        System.out.println(checkIp("192.168.1.1"));
        System.out.println(checkPort("1255a"));
        System.out.println(checkId("aaaaaaaaa11111111"));
    }

    private static boolean checkPort(String str) {
        try {
            String check = "^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5])$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }
    private static boolean checkId(String str) {
        try {
            String check = "[a-zA-Z]{9}\\d{8}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    private static  boolean checkIp(String str) {
        try {
            String check = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
//            String check = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }
}
