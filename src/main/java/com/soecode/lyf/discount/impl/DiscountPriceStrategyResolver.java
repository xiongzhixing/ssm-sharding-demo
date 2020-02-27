package com.soecode.lyf.discount.impl;

import com.soecode.lyf.discount.DiscountEnum;
import com.soecode.lyf.discount.DiscountStrategyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.math.BigDecimal;

@Component
public class DiscountPriceStrategyResolver implements DiscountStrategyResolver<BigDecimal> {
    @Override
    public boolean support(DiscountEnum discountEnum) {
        return discountEnum == DiscountEnum.DISCOUNTPRICE;
    }

    @Override
    public BigDecimal calculate(BigDecimal oriPrice,BigDecimal discountPrice) {
        return discountPrice;
    }

    public static void main(String[] args) {
        System.out.println(new DiscountPriceStrategyResolver().support(null));
    }
}
