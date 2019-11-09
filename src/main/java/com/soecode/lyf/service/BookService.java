package com.soecode.lyf.service;

import java.util.List;

import com.soecode.lyf.entity.deal.BookDO;

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
	BookDO getById(long bookId);

	/**
	 * 查询所有图书
	 * 
	 * @return
	 */
	List<BookDO> getList();

	int batchInsert() throws Exception;
}
