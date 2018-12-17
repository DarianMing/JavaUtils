//package com.lm.demo.mock;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
////https://www.cnblogs.com/wuyun-blog/p/7081548.html
////https://blog.csdn.net/xiaoxufox/article/details/78562656
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Book.class})
//@PowerMockIgnore("javax.management.*")
//@SpringBootTest
//public class BookTest {
//
//    @Test
//    public void test () {
//        Book stub = PowerMockito.mock(Book.class);
//        PowerMockito.when(stub.getResult()).thenReturn(5);
//        //System.out.println(stub.getResult());
//        //PowerMockito.doNothing().when(stub).getResult();
//        //PowerMockito.doThrow(new Exception()).when(stub).thenReturn(10);
//    }
//}
