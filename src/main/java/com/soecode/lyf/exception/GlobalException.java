package com.soecode.lyf.exception;

import com.soecode.lyf.dto.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleCustomerException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            stringBuilder.append(++i + ".").append(objectError.getDefaultMessage() + ".  ");
        }
        return Result.fail(Result.ERR_PARAM_INVALID, stringBuilder.toString());
    }

    @ExceptionHandler(Exception.class)
    public Object handleCustomerException(Exception e) {
        return Result.fail(Result.ERR_SYSTEM_EXCEPTION, e.getMessage());

    }
}
