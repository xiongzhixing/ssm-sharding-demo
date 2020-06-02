/*
package com.soecode.lyf.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

*/
/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/6/2 0002
 **//*

public class StyleParseUtil {

    public static <T> T parse(JSONObject jsonObject, Map<String,String> proMap,Class<T> target){
        if(Objects.isNull(jsonObject)){
            return null;
        }

    }

    private static void jsonObjectToMap(JSONObject jsonObject,Map<String,Object> map) throws Exception {
        if(Objects.isNull(jsonObject)){
            return;
        }
        for(Map.Entry<String,Object> entry:jsonObject.entrySet()){
            Class valCls = entry.getValue().getClass();
            if(valCls.getClassLoader() == null) {
                // JAVA 类型
                boolean isBaseType = setBaseType(map,valCls,entry);
                if(isBaseType){
                    continue;
                }
                if(valCls.getName().contains("List")){
                    //List 集合，基本类型数组或者是JSONObject数组
                    if(valCls.getClassLoader() != null){
                        //自定义类型
                        jsonObjectToMap.getWriteMethod().invoke(t,Arrays.asList(construct(paramCls)));
                    }else{
                        setBaseType(t, propertyDescriptor, proCls,true);
                    }
                }else{
                    throw new RuntimeException("未知的java类型：" + valCls.getName());
                }
            }else{
                //自定义类型
            }
        }
    }

    private void parseKeyAndValByVar(Map<String,Object> keyAndValue,String codeId,Object object){
        if(object == null){
            return;
        }
        if(object instanceof JSONObject){
            for(Map.Entry<String,Object> entry: ((JSONObject)object).entrySet()){
                if(entry.getValue() instanceof String){
                    String oneCodeId = (StringUtils.isEmpty(codeId) ? "" : codeId + "-") + entry.getKey();
                    if(keyAndValue.get(oneCodeId) == null){
                        keyAndValue.put(oneCodeId,entry.getValue());
                    }else if(keyAndValue.get(oneCodeId) instanceof String){
                        keyAndValue.put(oneCodeId, getList((String)keyAndValue.get(oneCodeId),(String)entry.getValue()));
                    }else if(keyAndValue.get(oneCodeId) instanceof List){
                        ((List) keyAndValue.get(oneCodeId)).add(entry.getValue());
                    }
                }else if(entry.getValue() instanceof JSONObject || entry.getValue() instanceof JSONArray){
                    parseKeyAndValByVar(keyAndValue,(StringUtils.isEmpty(codeId) ? "" : codeId + "-") + entry.getKey(),entry.getValue());
                }
            }
        }else if(object instanceof JSONArray){
            for(Object obj:((JSONArray)object)){
                if(obj instanceof String){
                    String oneCodeId = codeId;
                    if(keyAndValue.get(oneCodeId) == null){
                        keyAndValue.put(oneCodeId,obj);
                    }else if(keyAndValue.get(oneCodeId) instanceof String){
                        keyAndValue.put(oneCodeId, getList((String)keyAndValue.get(oneCodeId),(String)obj));
                    }else if(keyAndValue.get(oneCodeId) instanceof List){
                        ((List) keyAndValue.get(oneCodeId)).add(obj);
                    }
                }else if(object instanceof JSONObject || object instanceof JSONArray){
                    parseKeyAndValByVar(keyAndValue,codeId,obj);
                }
            }
        }
    }

    private static <T> boolean setBaseType(Map<String,Object> map, Class valCls,Map.Entry<String,Object> entry) throws Exception{
        if(Boolean.class == valCls || boolean.class == valCls ||
                Byte.class == valCls || byte.class == valCls ||
                Character.class == valCls || char.class == valCls ||
                Short.class == valCls || short.class == valCls ||
                Integer.class == valCls || int.class == valCls ||
                Long.class == valCls || long.class == valCls ||
                Float.class == valCls || float.class == valCls ||
                Double.class == valCls || double.class == valCls ||
                String.class == valCls ||
                Date.class == valCls){
            map.put(entry.getKey(),entry.getValue());
            return true;
        }
        return false;
    }
}*/
