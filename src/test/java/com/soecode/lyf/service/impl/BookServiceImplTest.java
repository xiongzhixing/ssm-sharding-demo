package com.soecode.lyf.service.impl;

import com.soecode.lyf.BaseTest;
import com.soecode.lyf.service.BookService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

public class BookServiceImplTest extends BaseTest {

	@Resource
	private BookService bookService;

	@Test
	public void testAppoint() throws Exception {
		long bookId = 1001;
		long studentId = 12345678910L;
		//AppointExecution execution = bookService.appoint(bookId, studentId);
		//System.out.println(execution);
	}

	@Test
	public void testBookId() throws Exception {
		long bookId = 1001;
		bookService.getById(bookId);
	}

	@Test
	public void testBatchInsert(){
		try {
			this.bookService.batchInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void test11() throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(1);

		new Thread(()->{
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//this.bookService.test1();
		}).start();

		new Thread(()->{
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//this.bookService.test2();
		}).start();

		countDownLatch.countDown();

		while(true){}
	}

}
