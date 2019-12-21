package com.soecode.lyf.manager;

import com.soecode.lyf.annotation.Cache;
import com.soecode.lyf.dao.BookDao;
import com.soecode.lyf.entity.Book;
import com.soecode.lyf.util.ConstructUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

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

    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(){
        int row = 0;
        for(int i = 0;i < 10;i++){
            Book book = ConstructUtil.construct(Book.class);
            if(new Random().nextInt(20) == 3){
                throw new RuntimeException("");
            }
            row += this.bookDao.insert(book);
        }
        return row;
    }
}
