package com.soecode.lyf.util;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.annotation.ExcelVOAttribute;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.security.SecureRandom;

public class ExcelUtilTest {
    public static void main(String[] args) throws Exception {
        ExcelUtil<Student> excelUtil = new ExcelUtil<Student>(Student.class);

        System.out.println(
                JSON.toJSONString(
                        excelUtil.importExcel("","C:\\Users\\Administrator\\Desktop\\新建 XLS 工作表.xls","xls")
                )
        );
    }


    @Data
    @ToString(callSuper = true)
    public static class Student implements Serializable{
        private static final long serialVersionUID = 3226328472185740059L;

        @ExcelVOAttribute(name = "学生姓名", column = "A")
        private String name;

        @ExcelVOAttribute(name = "学生年龄", column = "B")
        private Integer age;
    }
}
