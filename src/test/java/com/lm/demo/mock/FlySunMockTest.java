//package com.lm.demo.mock;
//
//import org.junit.Assert;
//import org.junit.Test;
////import org.powermock.api.mockito.PowerMockito;
//
//import java.io.File;
//
//public class FlySunMockTest {
//    @Test
//    public void testCallArgumentInstance(){
//        //mock出入参File对象
//        File file = PowerMockito.mock(File.class);
//        FlySunDemo demo = new FlySunDemo();
//        PowerMockito.when(file.exists()).thenReturn(true);
//        Assert.assertTrue(demo.callArgumentInstance(file));
//    }
//}
