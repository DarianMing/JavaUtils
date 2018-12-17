package com.lm.demo.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;

public class Test {

    private String s;

    public void before () {
        System.out.println(s);
        s = "测试前";
    }

    @Before
    public void test () {
        System.out.println(s);
        s = "测试中";
    }
}
