package com.soecode.lyf.messageconvert;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import sun.misc.IOUtils;

import java.io.IOException;

/**
 * @author Administrator
 */
public class EncryptJsonMessageConvert extends AbstractHttpMessageConverter<Object> {
    private static  final Logger logger = LoggerFactory.getLogger(EncryptJsonMessageConvert.class);

    public final static String ENCRYPTED_JSON_TYPE = "application/encrypt-json";
    public final static String ENCRYPTED_JSON_TYPE_UTF8 = "application/encrypt-json;charset=utf-8";

    private final static String AES_KEY = "qwerty";

    public EncryptJsonMessageConvert(){
        super(MediaType.valueOf(ENCRYPTED_JSON_TYPE),MediaType.valueOf(ENCRYPTED_JSON_TYPE_UTF8));
    }

    @Override
    protected boolean supports(Class<?> cls) {
        return true;
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        try{
            byte[] bytes = IOUtils.readFully(httpInputMessage.getBody(),-1,false);
            byte[] decryptBytes =  AESUtil.decrypt(bytes,AES_KEY);
            return JSON.parseObject(decryptBytes,aClass);
        }catch (Exception e){
            logger.error("EncryptJsonMessageConvert#readInternal catch a exception.",e);
            return null;
        }
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        try{
            byte[] encryptBytes = AESUtil.encrypt(JSON.toJSONString(object),AES_KEY);
            httpOutputMessage.getBody().write(encryptBytes);
        }catch (Exception e){
            logger.error("EncryptJsonMessageConvert#writeInternal catch a exception.",e);
        }
    }

    @Override
    public boolean canRead(Class clazz, MediaType mediaType) {
        for(MediaType supportMediaType:super.getSupportedMediaTypes()){
            if(supportMediaType.getType().equals(mediaType.getType()) &&
                    supportMediaType.getSubtype().equals(mediaType.getSubtype())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWrite(Class clazz, MediaType mediaType) {
        for(MediaType supportMediaType:this.getSupportedMediaTypes()){
            if(supportMediaType.getType().equals(mediaType.getType()) &&
                    supportMediaType.getSubtype().equals(mediaType.getSubtype())){
                return true;
            }
        }
        return false;
    }
}
