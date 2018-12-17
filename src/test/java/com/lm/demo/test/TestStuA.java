package com.lm.demo.test;

import org.junit.Assert;
import org.junit.Test;

//https://blog.csdn.net/qq_36505948/article/details/82797240
import static org.junit.Assert.fail;

public class TestStuA {

    //"此处输出提示语" 为错误时你个人想要输出的错误信息; 5  是指你期望的值；result 是指你调用程序后程序输出给你的结果
    @Test
    public void testA () {
        Assert.assertEquals("此处输出提示语" , 5 , 5);
        Assert.assertTrue("此处输出提示语" , true);
    }

    //在注解的时候添加expected 为忽略此异常
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testB (){
        int[] a = new int[2];
        System.out.println(a[3]);
    }

    //超时设置
    @Test(timeout = 5)
    public void testC () {
        while (true){

        }
    }

    //放在方法中，如果我顺利地执行，我就报失败出来。就是说按道理不应该执行到这里的，
    // 但是偏偏执行了,说明程序有问题
    @Test
    public void testD () {
        fail("Not yet implemented");
    }

    //assertNull与assertNotNull可以验证所测试的对象是否为空或不为空，
    //如果和预期的相同则测试成功，否则测试失败!
    @Test
    public void testE () {
        //Assert.assertNull("应该为空" , "aa");
        Assert.assertNotNull("不应该为空" , null);
    }
}
