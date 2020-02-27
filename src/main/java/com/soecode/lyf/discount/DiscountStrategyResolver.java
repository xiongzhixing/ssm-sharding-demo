package com.soecode.lyf.discount;

import java.math.BigDecimal;

public interface DiscountStrategyResolver<T> {

    boolean support(DiscountEnum discountEnum);

    BigDecimal calculate(BigDecimal oriPrice,T bizParam);
}
