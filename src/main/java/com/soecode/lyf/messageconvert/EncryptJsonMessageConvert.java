package com.soecode.lyf.messageconvert;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.util.AESUtil;
import com.soecode.lyf.vo.BaseVo;
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
import java.nio.charset.Charset;

/**
 * @author Administrator
 */
public class EncryptJsonMessageConvert extends AbstractHttpMessageConverter<BaseVo> {
    private static  final Logger logger = LoggerFactory.getLogger(EncryptJsonMessageConvert.class);

    public final static String ENCRYPTED_JSON_TYPE = "application/encrypt-json";
    public final static String ENCRYPTED_JSON_TYPE_UTF8 = "application/encrypt-json;charset=utf-8";

    private final static String AES_KEY = "qwerty";

    public EncryptJsonMessageConvert(){
        super(Charset.forName("utf-8"),MediaType.valueOf(ENCRYPTED_JSON_TYPE),MediaType.valueOf(ENCRYPTED_JSON_TYPE_UTF8));
    }

    @Override
    protected boolean supports(Class<?> cls) {
        return BaseVo.class.isAssignableFrom(cls);
    }

    @Override
    protected BaseVo readInternal(Class<? extends BaseVo> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        try{
            byte[] bytes = IOUtils.readNBytes(httpInputMessage.getBody(),-1);
            byte[] decryptBytes =  AESUtil.decrypt(bytes,AES_KEY);
            return JSON.parseObject(decryptBytes,aClass);
        }catch (Exception e){
            logger.error("EncryptJsonMessageConvert#readInternal catch a exception.",e);
            return null;
        }
    }

    @Override
    protected void writeInternal(BaseVo baseVo, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        try{
            byte[] encryptBytes = AESUtil.encrypt(JSON.toJSONString(baseVo),AES_KEY);
            httpOutputMessage.getBody().write(encryptBytes);
        }catch (Exception e){
            logger.error("EncryptJsonMessageConvert#writeInternal catch a exception.",e);
        }
    }
}
