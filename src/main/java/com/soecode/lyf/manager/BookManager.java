package com.soecode.lyf.manager;

import com.soecode.lyf.dao.deal.BookDao;
import com.soecode.lyf.entity.deal.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookManager{
    @Autowired
    private BookDao bookDao;

    //@Cache(result = Book.class)
    public Book queryById(long id) {
        return this.bookDao.queryById(id);
    }
    //@Cache(result = List<Book>.class)
    public List<Book> queryAll(int offset,int limit){
        return this.bookDao.queryAll(offset,limit);
    }
}
