package com.soecode.lyf.web;

import com.soecode.lyf.util.MD5Util;
import com.soecode.lyf.util.SignatureUtil;
import com.soecode.lyf.vo.BookVo;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 
 * @author Kemin
 */
public class BookControllerTest  {

	@Test
	public void test(){
		Map<String,String> headMap = new HashMap<>();
		//headMap.put("Content-type","application/encrypt-json");
		headMap.put("Content-type","application/json");

		BookVo bookVo = new BookVo();
		bookVo.setAppKey("test");
		bookVo.setTimestamp(System.currentTimeMillis());
		bookVo.setBookName("java");
		bookVo.setBookId(1);

		bookVo.setSign(MD5Util.encrypt(SignatureUtil.getRequestParamStr(bookVo,new HashSet<>()));
		//HttpClientUtils.doPost("http://localhost:8080/book/list",headMap, JSON.);
	}
}
