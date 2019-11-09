package com.soecode.lyf.entity.deal;

import com.soecode.lyf.annotation.DocAnnotation;

import java.util.List;

/**
 * 图书实体
 */
public class Book {
	@DocAnnotation(comment="书的id")
	private long bookId;// 图书ID
	@DocAnnotation(comment="书名")
	private String name;// 图书名称
	@DocAnnotation(comment = "书的数量")
	private int number;// 馆藏数量

	public Book() {
	}

	public Book(long bookId, String name, int number) {
		this.bookId = bookId;
		this.name = name;
		this.number = number;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", name=" + name + ", number=" + number + "]";
	}


}
