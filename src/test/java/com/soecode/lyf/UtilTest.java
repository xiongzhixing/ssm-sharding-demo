package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.util.CopyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class UtilTest {
    public static void main(String[] args) throws IOException {
        //Date
        System.out.println(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println(FastDateFormat.getInstance("yyyy-MM-dd").format(DateUtils.addDays(new Date(),3)));

        //String
        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");

        System.out.println(StringUtils.join(stringList.stream().map(str -> Integer.valueOf(str)).collect(Collectors.toList()),","));
        System.out.println(StringUtils.reverse("123"));
        System.out.println(StringUtils.abbreviate("123456789",4));
        System.out.println(StringUtils.appendIfMissing("123456789","789"));
        System.out.println(StringUtils.rightPad("123456789",20,"0"));
        System.out.println(StringUtils.leftPad("123456789",20,"0"));
        System.out.println(StringUtils.center("123456789",1));
        System.out.println(StringUtils.leftPad("123456789",2));

        //File
        File file = new File("C:\\Users\\Administrator\\Desktop\\a");
        File mergeFile = new File("C:\\Users\\Administrator\\Desktop\\a\\merge.txt");
        for(File tFile:file.listFiles()){
            FileUtils.write(mergeFile,FileUtils.readFileToString(tFile, "utf-8"),"utf-8",true);
        }

        //Object
        CopyUtil.People people = new CopyUtil.People();
        people.setAge(13L);

        System.out.println(JSON.toJSONString(CopyUtil.getObject(people,CopyUtil.People1.class)));

        List<CopyUtil.People> peopleList = new ArrayList<>();
        peopleList.add(CopyUtil.People.builder().age(13L).build());
        peopleList.add(CopyUtil.People.builder().age(114L).build());

        System.out.println(JSON.toJSONString(CopyUtil.getList(peopleList,CopyUtil.People1.class)));


        //

    }
}
