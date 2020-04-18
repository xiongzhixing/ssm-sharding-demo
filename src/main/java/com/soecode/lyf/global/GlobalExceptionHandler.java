package com.soecode.lyf.global;

import com.soecode.lyf.constant.ErrCode;
import com.soecode.lyf.dto.Result;
import com.soecode.lyf.entity.Book;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/4/18 0018
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) throws Exception {
        //e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        if(e == null){
            return Result.fail(ErrCode.ERR_PARAM_INVALID,"");
        }
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger idx = new AtomicInteger(1);
        e.getBindingResult().getAllErrors().stream().forEach( temp -> {
            stringBuilder.append(idx.getAndIncrement()).append(".").append(temp.getDefaultMessage()).append(" ");
        });

        return Result.fail(ErrCode.ERR_PARAM_INVALID,stringBuilder.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) throws Exception {
        //e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        if(e == null){
            return Result.fail(ErrCode.ERR_PARAM_INVALID,"");
        }
        return Result.fail(ErrCode.ERR_PARAM_INVALID,e.getMessage());
    }
}