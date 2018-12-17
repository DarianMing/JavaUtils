package com.lm.demo.controller;

import com.lm.demo.entity.Student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ExcelTool;

@Controller
public class ExcekController {

    public static void main(String[] args) throws Exception {
        //exportOut();
        exportIn();
    }

    private static void exportIn () throws Exception {
        InputStream in = new FileInputStream("c://aa.xlsx");
        Map<String , String> fields = new HashMap<>();
        fields.put("学号" , "uid");
        fields.put("姓名" , "uname");
        fields.put("性别" , "sex" );
        fields.put( "汉族" , "nation");
        List<Student> list = ExcelTool.ExecltoList(in , Student.class , fields);
        System.out.println(list.toArray());
    }

//    private static void exportOut() throws Exception {
//        OutputStream out = new FileOutputStream("c://aa.xlsx");
//        Student student = Student.builder()
//                .uid("1").uname("测试君").sex("男").nation("汉族").build();
//        List<Student> li = new ArrayList<>();
//        li.add(student);
//        Map<String , String> fields = new HashMap<>();
//        fields.put("uid" , "学号");
//        fields.put("uname" , "姓名");
//        fields.put("sex" , "性别");
//        fields.put("nation" , "汉族");
//        ExcelTool.ListtoExecl(li , out , fields);
//        out.close();
//    }
}
