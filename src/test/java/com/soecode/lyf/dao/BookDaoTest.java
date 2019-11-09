package com.soecode.lyf.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.dao.deal.BookDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.soecode.lyf.BaseTest;
import com.soecode.lyf.entity.deal.Book;

public class BookDaoTest extends BaseTest {

	@Autowired
	private BookDao bookDao;

	@Test
	public void testQueryById() throws Exception {
		long bookId = 1000;
		Book book = bookDao.queryById(bookId);
		System.out.println(book);
	}

	@Test
	public void testQueryAll() throws Exception {
		List<Book> books = bookDao.queryAll(0, 4);
		for (Book book : books) {
			System.out.println(book);
		}
	}

	@Test
	public void testReduceNumber() throws Exception {
		long bookId = 1000;
		int update = bookDao.reduceNumber(bookId);
		System.out.println("update=" + update);
	}

	@Test
	public void testByName() throws Exception {
		System.out.println(JSON.toJSONString(bookDao.queryByName("'asd' union select 4,'java',123")));
	}

	@Test
	public void testInsert() throws Exception {
		Book book = new Book();
		book.setBookId(1009);
		book.setName("java");
		book.setNumber(1000);

		System.out.println(JSON.toJSONString(bookDao.insert(book)));
	}

	@Test
	public void testQueryByBookIdLiSt(){
		List<Integer> bookIdList = new ArrayList<>();
		for(int i = 7;i < 30;i++){
			bookIdList.add(i);
		}

		System.out.println(JSON.toJSONString(this.bookDao.queryByBookIdList(bookIdList)));
	}

}
