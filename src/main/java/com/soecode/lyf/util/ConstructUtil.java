package com.soecode.lyf.util;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.entity.Book;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author Administrator
 */
public class ConstructUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConstructUtil.class);

    public static <T> T construct(Class<T> cls){
        if (cls == null) {
            return null;
        }
        try{
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
        }catch(Exception e){
            logger.error("ConstructUtil#construct catch a exception.",e);
            return null;
        }

    }

    private static <T> boolean setBaseType(T t, PropertyDescriptor propertyDescriptor, Class proCls,boolean isArray) throws Exception{
        if(Boolean.class == proCls || boolean.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateBoolean()):generateBoolean());
            return true;
        }else if(Byte.class == proCls || byte.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateByte()):generateByte());
            return true;
        }else if(Character.class == proCls || char.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateChar()):generateChar());
            return true;
        }else if(Short.class == proCls || short.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateShort()):generateShort());
            return true;
        }else if(Integer.class == proCls || int.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateInteger()):generateInteger());
            return true;
        }else if(Long.class == proCls || long.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateLong()):generateLong());
            return true;
        }else if(Float.class == proCls || float.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateFloat()):generateFloat());
            return true;
        }else if(Double.class == proCls || double.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateDouble()):generateDouble());
            return true;
        }else if(String.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateString()):generateString());
            return true;
        }else if(Date.class == proCls){
            propertyDescriptor.getWriteMethod().invoke(t,isArray ? Arrays.asList(generateDate()):generateDate());
            return true;
        }
        return false;
    }

    private static boolean generateBoolean(){
        return ((int)(Math.random() * 2) == 0) ? false : true;
    }

    private static byte generateByte(){
        return (byte)((int)(Math.random() * 256) - 128);
    }

    private static char generateChar(){
        int temp = (int)(Math.random() * 26);
        return (char)(temp + (temp % 2 == 0 ? 65 : 97));
    }

    private static short generateShort(){
        return (short)((int)(Math.random() * Math.pow(2,16)) + Short.MIN_VALUE);
    }

    public static double generateDouble() {
        return Double.MIN_VALUE + ((Double.MAX_VALUE - Double.MIN_VALUE) * new Random().nextDouble());
    }

    public static float generateFloat() {
        return Float.MIN_VALUE + ((Float.MAX_VALUE - Float.MIN_VALUE) * new Random().nextFloat());
    }

    private static  String generateString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < ((int)(Math.random() * 20) + 5);i++){
            stringBuilder.append(generateChar());
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
        System.out.println(JSON.toJSONString(ConstructUtil.construct(People.class)));
        System.out.println(JSON.toJSONString(ConstructUtil.construct(Women.class)));
        System.out.println(JSON.toJSONString(ConstructUtil.construct(Book.class)));
    }



    @Data
    @ToString(callSuper = true)
    static class People{
        private boolean a;
        private Boolean b;
        private byte c;
        private Byte d;
        private char e;
        private Character f;
        private short g;
        private Short h;
        private int i;
        private Integer j;
        private Long k;
        private long l;
        private float m;
        private Float n;
        private Double o;
        private double p;
        private String q;
        private Date r;

        private Dog s;
        private List<Dog> t;

        //private Set<String> u;


    }

    @Data
    @ToString(callSuper = true)
    static class Women extends People{
        private List<Dog> v;
    }

    @Data
    @ToString(callSuper = true)
    static class Dog{
        private String name;
    }
}
