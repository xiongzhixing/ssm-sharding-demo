package com.soecode.lyf.util;


import com.alibaba.fastjson.JSON;
import com.soecode.lyf.vo.BookVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.DigestUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

public class SignatureUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

    public static List<String> getRequestParamList(Object o, Set<String> excludeProperties) throws Exception {
        List<String> properties = new ArrayList<>();
        if (o == null) {
            return properties;
        }
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(o.getClass());
        try {

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if(excludeProperties != null && excludeProperties.contains(propertyName)){
                    continue;
                }
                String propertyVal = String.valueOf(propertyDescriptor.getReadMethod().invoke(o, new Object[0]));
                if(StringUtils.isBlank(propertyVal)){
                    continue;
                }
                properties.add(propertyName + "=" + propertyVal);
            }
            Collections.sort(properties);
            return properties;
        } catch (Exception e) {
            logger.error("SignatureUtil#getRequestParam catch a exception.",e);
            throw e;
        }
    }

    public static String getRequestParamStr(Object o, Set<String> excludeProperties) throws Exception {
        List<String> keyValList = getRequestParamList(o,excludeProperties);
        return keyValList.stream().collect(Collectors.joining("&"));
    }

    public static void main(String[] args) throws Exception {
        BookVo bookVo = new BookVo();
        bookVo.setAppKey("appKey");
        bookVo.setTimestamp(System.currentTimeMillis());
        //bookVo.setSign();
        bookVo.setNonce(System.currentTimeMillis());

        bookVo.setBookId(1000);
        bookVo.setBookName("java");


        String str = SignatureUtil.getRequestParamStr(bookVo,new HashSet<>(Arrays.asList("sign","appKey")));
        StringBuilder signStr = new StringBuilder(str).append("&k=").append(bookVo.getAppKey());
        logger.info("sign str={}",signStr.toString());
        String sign = DigestUtils.md5DigestAsHex(signStr.toString().getBytes());
        bookVo.setSign(sign);

        System.out.println(JSON.toJSONString(bookVo));
    }

}
