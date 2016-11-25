package com.example;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by living on 2016/1/14.
 */
public class Test1 {
    public static void main(String[] args) {
        test2();
//        test1();
    }

    private static void test2() {
        List<String> l = new ArrayList<>();
        l.add("a");
        l.add("b");
        l.add("c");
        l.add("d");
        l.remove(1);
        l.add(1,"zz");

        for (String s:l) {
            System.out.println(s);
        }
    }

    private static void test1() {
        String string = "{\"CollectValueStruct\":[{\"Cur\":-110,\"Temp\":18.5,\"Vol\":2.1349999904632568}]," +
                "\"FlagList\":[{\"Flag0\":0,\"Flag1\":0,\"Flag2\":0,\"Flag3\":0,\"Flag4\":0,\"Flag5\":0,\"Flag6\":0,\"Flag7\":0}]}\n";

        Gson gson = new Gson();
        ReadStruct readStruct = gson.fromJson(string, ReadStruct.class);
        ArrayList<CollectValueStruct> c = readStruct.CollectValueStruct;
        ArrayList<FlagList> f = readStruct.FlagList;
        System.out.println(c.size());
    }

}
