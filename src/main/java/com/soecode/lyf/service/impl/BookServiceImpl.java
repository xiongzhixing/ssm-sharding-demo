package com.soecode.lyf.service.impl;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.dao.AppointmentDao;
import com.soecode.lyf.dao.BookDao;
import com.soecode.lyf.dto.AppointExecution;
import com.soecode.lyf.entity.Appointment;
import com.soecode.lyf.entity.Book;
import com.soecode.lyf.entity.People;
import com.soecode.lyf.enums.AppointStateEnum;
import com.soecode.lyf.exception.AppointException;
import com.soecode.lyf.exception.NoNumberException;
import com.soecode.lyf.exception.RepeatAppointException;
import com.soecode.lyf.manager.BookManager;
import com.soecode.lyf.service.BaseService;
import com.soecode.lyf.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class BookServiceImpl extends BaseServiceImpl implements BookService,BaseService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookDao bookDao;

	@Autowired
	private BookManager bookManager;

	@Autowired
	private AppointmentDao appointmentDao;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;


	@Override
	public Book getById(long bookId) {
		return bookDao.queryById(bookId);
	}

	@Override
	public List<Book> getList() {
		return bookDao.queryAll(0, 1000);
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
	 * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	public AppointExecution appoint(long bookId, long studentId) {
		try {
			// 减库存
			int update = bookDao.reduceNumber(bookId);
			if (update <= 0) {// 库存不足
				throw new NoNumberException("no number");
			} else {
				// 执行预约操作
				int insert = appointmentDao.insertAppointment(bookId, studentId);
				if (insert <= 0) {// 重复预约
					throw new RepeatAppointException("repeat appoint");
				} else {// 预约成功
					Appointment appointment = appointmentDao.queryByKeyWithBook(bookId, studentId);
					return new AppointExecution(bookId, AppointStateEnum.SUCCESS, appointment);
				}
			}
		} catch (NoNumberException e1) {
			throw e1;
		} catch (RepeatAppointException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常转换为运行期异常
			throw new AppointException("appoint inner error:" + e.getMessage());
		}
	}

	@Override
	public void test(People people){
		List<String> paramMesList = validateParam(people);
		if(paramMesList.size() > 0){
			System.out.println(JSON.toJSONString(paramMesList));
			return;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void test1(){
		//1000	12345678910	2018-03-31 09:50:40
		this.bookDao.reduceNumber(1000);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.appointmentDao.updateAppointment(1000,12345678910L);
	}

	public void test2(){
		this.appointmentDao.updateAppointment(1000,12345678910L);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.bookDao.reduceNumber(1000);
	}

	@Transactional(rollbackFor = Exception.class)
	public void test3() throws ExecutionException, InterruptedException {
		List<Future<Integer>> futureList = new ArrayList<>();
		for(int i = 0;i < 3;i++){
			futureList.add(this.threadPoolTaskExecutor.submit(new Callable<Integer>() {
				@Override
				public Integer call() {
					return bookManager.batchInsert();
				}
			}));
		}

		int success = 0;
		for(Future<Integer> future:futureList){
			success += future.get();
		}

		if(success != 30){
			throw new RuntimeException("asdsd");
		}
	}
}
