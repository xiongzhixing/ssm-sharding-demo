package com.soecode.lyf.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.soecode.lyf.util.HttpClientUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kemin
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
/*@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional*/ public class BookControllerTest extends AbstractContextControllerTests {

    private MockMvc mockMvc;
    private String listUrl = "/book/list";
    private String detailUrl = "/book/{bookId}/detail";
    private String appointUrl = "/book/{bookId}/appoint";
    private long bookId = 1000;

	/*@Before
    public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).alwaysDo(print()).build();
	}*/

    @Test
    public void list() throws Exception {
        this.mockMvc.perform(get(listUrl)).andExpect(view().name("list"));
    }

    @Test
    public void existDetail() throws Exception {
        this.mockMvc.perform(get(detailUrl, bookId)).andExpect(view().name("detail")).andExpect(model().attributeExists("book"));
    }

    @Test
    public void notExistDetail() throws Exception {
        this.mockMvc.perform(get(detailUrl, 1100)).andExpect(forwardedUrl("/book/list"));
    }

    @Test
    public void appointTest() throws Exception {
        this.mockMvc.perform(post(appointUrl, bookId).param("studentId", "1").accept(MediaType.APPLICATION_JSON)).andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    public void test() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));

        for(int i=0;i < 5;i++){
            threadPoolExecutor.execute(() -> {
                try {
                    String url = "http://localhost:8080/book/206/detail";

                    Map<String, String> headMap = new HashMap<>();
                    headMap.put("Content-type", "application/json");

                    System.out.println(HttpClientUtils.doGet(url, headMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        while(true){}

    }


}
