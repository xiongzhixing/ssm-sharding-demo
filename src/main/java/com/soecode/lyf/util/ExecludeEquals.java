package com.soecode.lyf.util;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 场景：update数据库时，避免相同的值在set，造成数据库资源消耗
 */
public class ExecludeEquals {
    private static final Logger logger = LoggerFactory.getLogger(ExecludeEquals.class);

    public static <T> T setNullEqualsVal(T target,T source,Set<String> excludeProperties){
        if(target == null || source == null){
            return target;
        }
        Field[] tFields = getFields(target.getClass()).toArray(new Field[0]);
        for(Field tField:tFields){
            if(tField == null){
                continue;
            }
            if(excludeProperties.contains(tField.getName())){
                continue;
            }
            try {
                Field sField = getField(source.getClass(),tField.getName());
                if(tField.getType() != sField.getType()){
                    continue;
                }
                tField.setAccessible(true);
                sField.setAccessible(true);
                Object targetVal = tField.get(target);
                Object sourceVal = sField.get(source);
                if(targetVal != null && targetVal.equals(sourceVal)){
                    tField.set(target,null);
                }
            } catch (Exception e) {
                logger.error("ExecludeEquals#setNullEqualsVal catch a exception.name={}",tField.getName(),e);
            }
        }

        return target;
    }

    private static List<Field> getFields(Class cls){
        List<Field> fieldList = new ArrayList<>();
        if(cls == null){
            return fieldList;
        }
        fieldList.addAll(getFields(cls.getSuperclass()));
        fieldList.addAll(Arrays.asList(cls.getDeclaredFields()));
        return fieldList;
    }
    private static Field getField(Class cls,String name){
        List<Field> fieldList = getFields(cls);
        return fieldList.stream().filter(temp -> (temp != null && temp.getName().equals(name))).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        People target = People.builder()
                .id(1)
                .age(25)
                .name("xzx")
                .height(177)
                .build();

        People source = People.builder()
                .id(1)
                .age(26)
                .name("xzx")
                .height(176)
                .build();

        System.out.println(ExecludeEquals.setNullEqualsVal(target,source,new HashSet<>(Arrays.asList("id"))));
    }

    @ToString
    @Data
    @Builder
    static class People {
        private Integer id;
        private Integer age;
        private String name;
        private Integer height;
    }
 /*   @ToString
    @Data
    @Builder
    static class Women extends People{
        private String shoe;
    }*/
}
