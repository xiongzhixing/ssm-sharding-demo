package com.soecode.lyf.web;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.util.AESUtil;
import com.soecode.lyf.util.HttpClientUtil;
import com.soecode.lyf.util.SignatureUtil;
import com.soecode.lyf.vo.BookVo;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
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
		headMap.put("Content-type","application/encrypt-json;charset=utf-8");
		headMap.put("Accept","application/encrypt-json;charset=utf-8");
		//headMap.put("Content-type","application/json");
		//headMap.put("Accept","application/json");

		BookVo bookVo = new BookVo();
		bookVo.setAppKey("test");
		bookVo.setTimestamp(System.currentTimeMillis());
		bookVo.setBookName("java");
		bookVo.setBookId(1);

		bookVo.setSign(DigestUtils.md5DigestAsHex(SignatureUtil.getRequestParamStr(bookVo,new HashSet<>(Arrays.asList("sign"))).getBytes()));

		System.out.println(HttpClientUtil.doPost("http://localhost:8080/book/list",headMap, AESUtil.encrypt(JSON.toJSONString(bookVo),"qwerty")));
		//System.out.println(HttpClientUtil.doPost("http://localhost:8080/book/list",headMap, JSON.toJSONString(bookVo)));


	}
}
