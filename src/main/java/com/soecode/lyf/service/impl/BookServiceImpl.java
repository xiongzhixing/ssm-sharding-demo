package com.soecode.lyf.service.impl;

import com.soecode.lyf.dao.deal.BookDao;
import com.soecode.lyf.dao.idgenerate.BookIdGenerateDao;
import com.soecode.lyf.entity.deal.Book;
import com.soecode.lyf.service.BaseService;
import com.soecode.lyf.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookServiceImpl extends BaseServiceImpl implements BookService,BaseService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private BookDao bookDao;

	@Autowired
	private BookIdGenerateDao bookIdGenerateDao;

	@Override
	public Book getById(long bookId) {
		return bookDao.queryById(bookId);
	}

	@Override
	public List<Book> getList() {
		return bookDao.queryAll(0, 1000);
	}

	@Override
	@Transactional(value = "shardingManager",rollbackFor=Exception.class)
	public int batchInsert() {
		for(int i =0;i < 100;i++){
			//BookIdGenerate bookIdGenerate = new BookIdGenerate();
			//bookIdGenerate.setName("a");
			//this.bookIdGenerateDao.replace(bookIdGenerate);
			Book book = new Book();
			//book.setBookId(bookIdGenerate.getId());
			book.setName("java");
			book.setNumber(888);
			this.bookDao.insert(book);
		}
		return 0;
	}

	public void test1(){
		//1000	12345678910	2018-03-31 09:50:40
		this.bookDao.reduceNumber(1000);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void test2(){
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.bookDao.reduceNumber(1000);
	}

}
