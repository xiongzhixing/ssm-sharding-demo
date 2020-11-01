package com.soecode.lyf.manage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.soecode.lyf.dao.BookDao;
import com.soecode.lyf.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CacheManage {
    @Autowired
    private BookDao bookDao;

    private LoadingCache<Long,Book> bookLoadingCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .refreshAfterWrite(20, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, Book>() {
                @Override
                public Book load(Long bookId) throws Exception {
                    return bookDao.queryById(bookId);
                }
            });

    public Book get(Long bookId){
        try {
            return bookLoadingCache.get(bookId);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
