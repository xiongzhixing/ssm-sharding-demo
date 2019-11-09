package com.soecode.lyf.manager;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.BaseTest;
import com.soecode.lyf.entity.Book;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookManageTester extends BaseTest {
    @Autowired
    private BookManager bookManager;

    @Test
    public void test() throws InterruptedException {
        System.out.println(System.currentTimeMillis());
        System.out.println(JSON.toJSONString(bookManager.queryById(1)));
        TimeUnit.SECONDS.sleep(10);
        System.out.println(JSON.toJSONString(bookManager.queryById(1)));
        TimeUnit.SECONDS.sleep(10);
        System.out.println(JSON.toJSONString(bookManager.queryById(1)));
        TimeUnit.SECONDS.sleep(10);
        System.out.println(JSON.toJSONString(bookManager.queryById(1)));
        TimeUnit.SECONDS.sleep(10);
        System.out.println(JSON.toJSONString(bookManager.queryById(1)));
    }

    @Test
    public void test11() throws InterruptedException {
        List<Book> bookList = bookManager.queryAll(1,10);
        System.out.println(JSON.toJSONString(bookList));
        TimeUnit.SECONDS.sleep(10);
        List<Book> bookList1 = bookManager.queryAll(1,10);
        System.out.println(JSON.toJSONString(bookList1));
        TimeUnit.SECONDS.sleep(10);
        List<Book> bookList2 = bookManager.queryAll(1,10);
        System.out.println(JSON.toJSONString(bookList2));
        TimeUnit.SECONDS.sleep(10);
        List<Book> bookList3 = bookManager.queryAll(1,10);
        System.out.println(JSON.toJSONString(bookList3));
        TimeUnit.SECONDS.sleep(10);
        List<Book> bookList4 = bookManager.queryAll(1,10);
        System.out.println(JSON.toJSONString(bookList4));
    }
}
