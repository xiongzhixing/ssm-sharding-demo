package com.soecode.lyf.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/9/12 0012
 **/
public class CrawlerUtil {
    // 地址
    private static final String URL = "https://user.qzone.qq.com/876841124?source=aiostar";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";

    private static final String OPEN_PATH = "C:\\Users\\Administrator\\Desktop\\img";

    // 获取html内容
    public static String getHTML(String srcUrl) throws Exception {
        URL url = new URL(srcUrl);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = br.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }
        br.close();
        isr.close();
        is.close();
        return buffer.toString();
    }

    // 获取image url地址
    public static List<String> getImageURL(String html) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    // 获取image src地址
    public static List<String> getImageSrc(List<String> listUrl) {
        List<String> listSrc = new ArrayList<String>();
        for (String img : listUrl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(img);
            while (matcher.find()) {
                listSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listSrc;
    }

    // 下载图片
    private static void Download(List<String> listImgSrc) {
        for (String url : listImgSrc) {
            downloadSingleImg(url);
        }
    }

    private static void downloadSingleImg(String url) {
        // 开始时间
        InputStream in = null;
        FileOutputStream fo = null;
        try {
            String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
            URL uri = new URL(url);

            in = uri.openStream();
            fo = new FileOutputStream(new File(OPEN_PATH + File.separatorChar + imageName));// 文件输出流
            byte[] buf = new byte[1024];

            int length = 0;
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {
        String html = getHTML(URL);
        List<String> listUrl = getImageURL(html);
        List<String> listSrc = getImageSrc(listUrl);
        Download(listSrc);
    }

}