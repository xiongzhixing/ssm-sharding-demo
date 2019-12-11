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
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")
        },
        threadPoolProperties = {
                @HystrixProperty(name="coreSize",value = "32"),
                @HystrixProperty(name="maxQueueSize",value = "128"),
                @HystrixProperty(name="queueSizeRejectionThreshold",value = "64"),
                @HystrixProperty(name="keepAliveTimeMinutes",value = "2")
        }
)
public class BookCommand {
    private static final Logger logger = LoggerFactory.getLogger(BookCommand.class);
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
