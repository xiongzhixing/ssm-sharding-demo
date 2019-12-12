package com.soecode.lyf.util;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ConstructUtil {
    private static List<Boolean> booleanList = Arrays.asList(true,false);

    public static <T> T construct(Class<T> cls) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (cls == null) {
            return null;
        }
        T t = cls.newInstance();
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(cls);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Class proCls = propertyDescriptor.getPropertyType();
            if(Boolean.class == proCls || boolean.class == proCls){
                propertyDescriptor.getWriteMethod().invoke(t,generateBoolean());
            }else if(String.class == proCls){
                propertyDescriptor.getWriteMethod().invoke(t,generateString());
            }else if(Date.class == proCls){
                propertyDescriptor.getWriteMethod().invoke(t,generateDate());
            }else if(Integer.class == proCls || int.class == proCls){
                propertyDescriptor.getWriteMethod().invoke(t,generateInteger());
            }else if(Long.class == proCls || long.class == proCls){
                propertyDescriptor.getWriteMethod().invoke(t,generateLong());
            }else{
                throw new RuntimeException("不支持的类型");
            }
        }
        return t;
    }

    private static boolean generateBoolean(){
        return booleanList.get((int)(Math.random() * 2));
    }

    private static  String generateString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < ((int)(Math.random() * 20) + 5);i++){
            int temp = (int)(Math.random() * 26);
            stringBuilder.append((char)(temp + (temp % 2 == 0 ? 65 : 97)));
        }
        return stringBuilder.toString();
    }

    private static Date generateDate(){
        Date now = new Date();
        return DateUtils.addDays(now,((int)(Math.random() * 365) - 365));
    }

    private static Integer generateInteger(){
        return (int)(Math.random() * Integer.MAX_VALUE);
    }

    private static Long generateLong(){
        return (long)(Math.random() * Long.MAX_VALUE);
    }

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(ConstructUtil.construct(People.class));
        System.out.println(ConstructUtil.construct(Women.class));
    }



    @Data
    @ToString(callSuper = true)
    static class People{
        private String name;
        private int c;
        private Boolean aBoolean;
        private boolean bBoolean;

    }

    @Data
    @ToString(callSuper = true)
    static class Women extends People{
        private Integer age;
        private Date date;
        private Long height;
        private long d;
    }
}
