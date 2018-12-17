package com.lm.demo.config;

import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//http://www.yuanrengu.com/index.php/springmvc-user-initbinder.html
//https://blog.csdn.net/zxfryp909012366/article/details/82955259
//https://www.cnblogs.com/magicalSam/p/7198420.html
public class BaseController {

    @InitBinder
    public void initBinder (WebDataBinder binder) {
        binder.registerCustomEditor(Date.class , new MyDateEditor());
        binder.registerCustomEditor(Double.class , new DoubleEditor());
        binder.registerCustomEditor(Integer.class , new IntegerEditor());
    }

    public class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(text);
            } catch (ParseException e) {
                format = new SimpleDateFormat( "yyyy-MM-dd");
                try {
                    date = format.parse(text);
                } catch (ParseException e1) {
                    //e1.printStackTrace();
                }
                //e.printStackTrace();
                date = null;
            }
            setValue(date);
        }
    }

    public class DoubleEditor extends PropertiesEditor {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if ( text == null || text.equals("")) {
                text = "0";
            }
            setValue(Double.parseDouble(text));
        }

        @Override
        public String getAsText() {
            return getValue().toString();
        }
    }

    public class IntegerEditor extends PropertiesEditor {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null || text.equals("")) {
                text = "0";
            }
            setValue(Integer.parseInt(text));
        }

        @Override
        public String getAsText() {
            return getValue().toString();
        }
    }
}
