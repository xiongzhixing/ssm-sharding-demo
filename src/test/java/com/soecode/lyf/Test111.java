package com.soecode.lyf;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/3/21 0021
 **/
public class Test111 {
    public static void main(String[] args) {
        System.out.println(People.class.getClassLoader());
        System.out.println(Integer.class.getClassLoader());
        System.out.println(BigDecimal.class.getClassLoader());
        System.out.println(List.class.getClassLoader());
        System.out.println(HashMap.class.getClassLoader());

        List<People> peopleList = Arrays.asList(
                People.builder()
                        .age(12)
                        .name("xzx")
                        .build(),
                People.builder()
                        .age(9)
                        .name("zs")
                        .build(),
                People.builder()
                        .age(11)
                        .name("ls")
                        .build()
        );

        //peopleList = null;
        System.out.println(
            Optional.ofNullable(peopleList).orElse(new ArrayList<>()).stream().collect(
                 Collectors.groupingBy(People::getAge,Collectors.mapping(People::getName, Collectors.toList()))
            )
        );

        System.out.println(
                Optional.ofNullable(peopleList).orElse(new ArrayList<>()).stream().collect(
                        Collectors.groupingBy(People::getAge,Collectors.counting())
                )
        );

        Map<Integer,Set<People>> peopleMap = Optional.ofNullable(peopleList).orElse(new ArrayList<>()).stream().collect(
                Collectors.groupingBy(People::getAge, Collectors.toSet())
        );
        System.out.println(peopleMap);

        Map<Boolean,List<People>> booleanListMap =  Optional.ofNullable(peopleList).orElse(new ArrayList<>()).stream().collect(
                Collectors.partitioningBy(people -> {
                    if(people == null || people.getAge() < 10){
                        return true;
                    }
                    return false;
                })
        );

        System.out.println(booleanListMap);

        Optional.ofNullable(peopleList).orElse(new ArrayList<>()).stream().collect(
                Collectors.mapping(People::getName,Collectors.toList())
        );
    }


}