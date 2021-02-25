package com.soecode.lyf.manager;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.soecode.lyf.entity.Book;
import com.soecode.lyf.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DefaultProperties(
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.strategy",value = "SEMAPHORE"),
                @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests",value = "10"),
                @HystrixProperty(name = "fallback.isolation.semaphore.timeoutInMilliseconds",value = "10")

        }
)
public class BookCommandSemaphore {
    private static final Logger logger = LoggerFactory.getLogger(BookCommandSemaphore.class);
    @Autowired
    private BookService bookService;

    @HystrixCommand(fallbackMethod = "fallback")
    public Book getById(long bookId) {
        return this.bookService.getById(bookId);
    }

    public Book fallback(long bookId) {
        logger.warn("BookCommand#fallback 承载超过极限，已启用hystrix");
        return null;
    }
}
