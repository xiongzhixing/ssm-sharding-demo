package com.soecode.lyf.web;

import java.util.List;
import java.util.stream.Collectors;

import com.soecode.lyf.annotation.DocAnnotation;
import com.soecode.lyf.vo.BookVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.soecode.lyf.dto.Result;
import com.soecode.lyf.entity.deal.Book;
import com.soecode.lyf.service.BookService;

import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/book") // url:/模块/资源/{id}/细分 /seckill/list
public class BookController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

	@RequestMapping(method=RequestMethod.POST,value = "/list")
	@ResponseBody
	@DocAnnotation(comment="查询列表方法")
	public Result<List<Book>> list(@RequestBody @Validated BookVo book,BindingResult result) {
		Result<List<Book>> res = new Result<>();
		if(result.hasErrors()){
			res.setErrMessage(result.getAllErrors().stream().filter(error -> error != null).map(error -> error.getDefaultMessage()).collect(Collectors.toList()).toString());
			return res;
		}
		List<Book> list = bookService.getList();
		res.setData(list);
		return res;
	}

	@DocAnnotation(comment = "查询详情方法")
	@RequestMapping(method = RequestMethod.GET,value = "/detail")
	@ResponseBody
	private Result<Book> detail(@RequestParam(value = "bookId") @NotNull(message = "bookId不能为空") Long bookId) {
		Book book = bookService.getById(bookId);
		return Result.success(book);
	}
}
