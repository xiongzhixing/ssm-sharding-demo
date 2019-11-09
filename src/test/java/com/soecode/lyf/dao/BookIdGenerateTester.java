package com.soecode.lyf.dao;

import com.soecode.lyf.BaseTest;
import com.soecode.lyf.dao.idgenerate.BookIdGenerateDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class BookIdGenerateTester extends BaseTest {

    @Autowired
    private BookIdGenerateDao bookIdGenerateDao;

    @Test
    public void test(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",1);
        params.put("name","xzx1");
        params.put("tabIndex","1");
        bookIdGenerateDao.replace(params);

        Map<String, Object> params2 = new HashMap<String, Object>();
        params.put("id",1);
        params2.put("name","xzx2");
        params2.put("tabIndex","2");
        bookIdGenerateDao.replace(params2);

        Map<String,Object> params3 = new HashMap<String,Object>();
        params.put("id",1);
        params3.put("name","xzx3");
        params3.put("tabIndex","3");
        bookIdGenerateDao.replace(params3);

        Map<String,Object> param4 = new HashMap<String,Object>();
        params.put("id",1);
        param4.put("name","xzx4");
        param4.put("tabIndex","4");
        bookIdGenerateDao.replace(param4);

        Map<String, Object> params5 = new HashMap<String, Object>();
        params.put("id",1);
        params5.put("name","xzx5");
        params5.put("tabIndex","5");
        bookIdGenerateDao.replace(params5);

    }

    @Test
    public void test1(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("tabIndex","1");
        bookIdGenerateDao.selectAll(params);
    }
}
