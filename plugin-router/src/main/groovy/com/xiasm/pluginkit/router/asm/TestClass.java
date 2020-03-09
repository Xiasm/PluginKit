package com.xiasm.pluginkit.router.asm;

public class TestClass {

    public void onCreate() {
        long start = System.currentTimeMillis();

        System.out.println("haha");

        long end = System.currentTimeMillis();
        long takeTimes = end - start;
        long seconds = takeTimes % 1000;
        long mills = takeTimes / 1000;
        System.out.println("共消耗" + seconds + "秒" + mills + "毫秒");
    }
}
