package com.soecode.lyf.util;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ConstructUtil {
    private static List<Boolean> booleanList = Arrays.asList(true,false);

    public static <T> T construct(Class<T> cls) throws Exception {
        if (cls == null) {
            return null;
        }
        T t = cls.newInstance();
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(cls);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Class proCls = propertyDescriptor.getPropertyType();
            if(proCls.getClassLoader() == null){
                // JAVA 类型
                boolean execute = setBaseType(t, propertyDescriptor, proCls,false);
                if(execute){
                    continue;
                }
                if(proCls.getName().contains("List")){
                    //List 集合
                    Parameter parameter = propertyDescriptor.getWriteMethod().getParameters()[0];
                    Class paramCls = (Class) ((ParameterizedType)parameter.getParameterizedType()).getActualTypeArguments()[0];
                    if(paramCls.getClassLoader() != null){
                        //自定义类型
                        propertyDescriptor.getWriteMethod().invoke(t,Arrays.asList(construct(paramCls)));
                    }else{
                        setBaseType(t, propertyDescriptor, proCls,true);
                    }
                }else{
                    throw new RuntimeException("未知的java类型：" + proCls.getName());
                }
            }else{
                //自定义类型
                propertyDescriptor.getWriteMethod().invoke(t,construct(proCls));
            }

        }
        return t;
    }

    private static <T> boolean setBaseType(T t, PropertyDescriptor propertyDescriptor, Class proCls,boolean isArray) throws Exception{
        if(Boolean.class == proCls || boolean.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateBoolean()):generateBoolean());
            return true;
        }else if(String.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateString()):generateString());
            return true;
        }else if(Date.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateDate()):generateDate());
            return true;
        }else if(Integer.class == proCls || int.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateInteger()):generateInteger());
            return true;
        }else if(Long.class == proCls || long.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateLong()):generateLong());
            return true;
        }
        return false;
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

    public static void main(String[] args) throws Exception {
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
        private List<Dog> dogList;
    }

    @Data
    @ToString(callSuper = true)
    static class Dog{
        private String name;
    }
}
