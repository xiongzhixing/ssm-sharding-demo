package com.soecode.lyf.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountStrategyResolverHolder {

    @Autowired
    private List<DiscountStrategyResolver> discountStrategyResolverList;

    public DiscountStrategyResolver getDiscountStrategyResolver(DiscountEnum discount){
        for(DiscountStrategyResolver discountStrategyResolver:discountStrategyResolverList){
            if(discountStrategyResolver.support(discount)){
                return discountStrategyResolver;
            }
        }
        throw new RuntimeException("not find class");
    }
}
