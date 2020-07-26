package com.soecode.lyf.discount.impl;

import com.soecode.lyf.discount.DiscountEnum;
import com.soecode.lyf.discount.DiscountStrategyResolver;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountSubtractStrategyResolver implements DiscountStrategyResolver<BigDecimal> {
    @Override
    public boolean support(DiscountEnum discountEnum) {
        return false;
    }

    @Override
    public BigDecimal calculate(BigDecimal oriPrice,BigDecimal discountSubstract) {
        return oriPrice.subtract(discountSubstract).setScale(2,BigDecimal.ROUND_UP);
    }
}