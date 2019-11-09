package com.soecode.lyf.manager;

import com.soecode.lyf.annotation.Cache;
import com.soecode.lyf.dao.BookDao;
import com.soecode.lyf.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookManager{
    @Autowired
    private BookDao bookDao;

    @Cache
    public Book queryById(long id) {
        return this.bookDao.queryById(id);
    }

    @Cache
    public List<Book> queryAll(int offset,int limit){
        return this.bookDao.queryAll(offset,limit);
    }
}
