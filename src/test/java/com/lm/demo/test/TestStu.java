package com.lm.demo.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStu {

    private static String s;

    @BeforeClass
    public static void beforeClass () {
        System.out.println(s);
        s = "我为s赋初值beforeClass";
    }

    @BeforeClass
    public static void beforeClassddd () {
        System.out.println(s);
        s = "我为s赋初值beforeClassddd";
    }

    @Before
    public void before () {
        System.out.println(s);
        s = "测试前before";
    }

    @Before
    public void before1 () {
        System.out.println(s);
        s = "测试前before1";
    }

    @Test
    public void test () {
        System.out.println(s);
        s = "测试中";
    }

    @Test
    public void testdd () {
        System.out.println(s);
        s = "测试中dd";
    }

    @After
    public void after () {
        System.out.println(s);
        s = "测试后after";
    }

    @After
    public void afterdd () {
        System.out.println(s);
        s = "测试后afterdd";
    }

    @AfterClass
    public static void AfterClass () {
        System.out.println(s);
        s = "我为s赋末值AfterClass";
    }

    @AfterClass
    public static void AfterClassdd () {
        System.out.println(s);
        s = "我为s赋末值AfterClassdd";
    }
}
