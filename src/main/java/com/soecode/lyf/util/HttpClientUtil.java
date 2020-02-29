package com.soecode.lyf.util;


import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xzx
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static HttpClientContext context = HttpClientContext.create();
    private static String CHARSET = "UTF-8";

    /**
     * @param url
     * @param headers
     * @param param
     * @return
     */
    public static byte[] doGet(String url,Map<String,String> headers,Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;

        byte[] result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            //设置请求头
            if(headers != null && headers.size() > 0){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    httpGet.setHeader(entry.getKey(),entry.getValue());
                }
            }
            // 执行请求
            try {
                response = httpclient.execute(httpGet);
                // 使用HttpClient认证机制
                // response = httpClient.execute(httpGet, context);
            } catch (Exception e) {
                log.warn("HttpClientUtil#doGet 请求失败,请求地址：{}", url);
                e.printStackTrace();
            }
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                result = IOUtils.toByteArray(response.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param url
     * @return
     */
    public static byte[] doGet(String url) {
        return doGet(url, null,null);
    }

    /**
     *
     * @param url
     * @param headers
     * @return
     */
    public static byte[] doGet(String url,Map<String,String> headers){
        return doGet(url,headers,null);
    }

    /**
     *
     * @param url
     * @param headers
     * @param param
     * @return
     */
    public static byte[] doPostMap(String url,Map<String,String> headers,Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        byte[] result = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            if(headers != null && headers.size() > 0){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    httpPost.setHeader(entry.getKey(),entry.getValue());
                }
            }
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, CHARSET);
                httpPost.setEntity(entity);
            }

            log.info("【POST请求信息】,请求地址:{},请求参数的MAP:{}", url, param);
            // 执行http请求
            try {
                response = httpClient.execute(httpPost);
                // 使用HttpClient认证机制
                // response = httpClient.execute(httpPost, context);
            } catch (Exception e) {
                log.warn("【POST请求失败】,请求地址:{},请求参数的MAP:{}", url, param);
                e.printStackTrace();
            }
            result = IOUtils.toByteArray(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * @param url
     * @return
     */
    public static byte[] doPost(String url) {
        return doPostMap(url, null,null);
    }

    /**
     *
     * @param url
     * @param headers
     * @return
     */
    public static byte[] doPost(String url,Map<String,String> headers) {
        return doPostMap(url, headers,null);
    }

    /**
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static byte[] doPostJson(String url,Map<String,String> headers,String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        byte[] result = null;

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            if(headers != null && headers.size() > 0){
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    httpPost.setHeader(entry.getKey(),entry.getValue());
                }
            }
            // 创建请求内容(指定请求形式是Json形式的字符串)
            // 在SpringMVC中接收请求的Json,需要使用@RequestBody;
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            try {
                response = httpClient.execute(httpPost);
                // 使用HttpClient认证机制
                // response = httpClient.execute(httpPost, context);
            } catch (Exception e) {
                log.warn("【POST请求(参数为Json)失败】,请求地址：{},参数：{}", url, json);
                e.printStackTrace();
            }
            result = IOUtils.toByteArray(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}