package com.soecode.lyf.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.soecode.lyf.dto.AppointExecution;
import com.soecode.lyf.entity.Book;
import com.soecode.lyf.entity.People;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务接口：站在"使用者"角度设计接口 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 */
public interface BookService {

	/**
	 * 查询一本图书
	 * 
	 * @param bookId
	 * @return
	 */
	Book getById(long bookId);

	/**
	 * 查询所有图书
	 * 
	 * @return
	 */
	List<Book> getList();

	/**
	 * 预约图书
	 * 
	 * @param bookId
	 * @param studentId
	 * @return
	 */
	AppointExecution appoint(long bookId, long studentId);

	void test(People people);

	public void test1();

	public void test2();

	public void test3() throws ExecutionException, InterruptedException;
}
