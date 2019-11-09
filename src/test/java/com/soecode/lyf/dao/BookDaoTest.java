package com.soecode.lyf.dao;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.BaseTest;
import com.soecode.lyf.dao.deal.BookDao;
import com.soecode.lyf.entity.deal.BookDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BookDaoTest extends BaseTest {

	@Autowired
	private BookDao bookDao;

	@Test
	public void testQueryById() throws Exception {
		long bookId = 1000;
		BookDO book = bookDao.queryById(bookId);
		System.out.println(book);
	}

	@Test
	public void testQueryAll() throws Exception {
		System.out.println();
		List<BookDO> books = bookDao.queryAll(0, 4);
		for (BookDO book : books) {
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
		BookDO book = new BookDO();
		book.setBookId(1009L);
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
