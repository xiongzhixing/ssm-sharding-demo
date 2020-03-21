package com.soecode.lyf;

import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestLamada {
    public static void main(String[] args) throws RuntimeException {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People("xzx",26));
        peopleList.add(new People("zs",27));
        peopleList.add(new People("ls",27));
        peopleList.add(new People("ww",null));
        peopleList.add(new People("zl",27));

        System.out.println(Optional.ofNullable(peopleList).orElse(Lists.newArrayList()).stream()
                .filter(Objects::nonNull).map(people -> people.getName() + " ").reduce(String::concat).get());

        System.out.println(Optional.ofNullable(peopleList).orElse(Lists.newArrayList()).stream()
                .filter(Objects::nonNull).map(People::getAge).filter(Objects::nonNull).reduce(Integer::sum).get());

        peopleList = null;
        try{
            System.out.println(Optional.ofNullable(peopleList).orElse(Lists.newArrayList()).stream()
                    .filter(Objects::nonNull).map(people -> people.getName() + " ").reduce(String::concat).orElseThrow(() -> new RuntimeException("系统运行异常")));
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            System.out.println(Optional.ofNullable(peopleList).orElse(Lists.newArrayList()).stream()
                    .filter(Objects::nonNull).map(People::getAge).filter(Objects::nonNull).reduce(Integer::sum).orElseThrow(() -> new RuntimeException("系统异常")));
        }catch(Exception e){
            System.out.println(e);
        }

        List<People> peopleList1 = new ArrayList<>();
        peopleList1.add(new People("qq",26));
        peopleList1.add(new People("wb",27));
        peopleList1.add(new People("xj",27));
        peopleList1.add(new People("qs",null));

        System.out.println(Stream.of(peopleList,peopleList1).collect(Collectors.toList()));


        List<People> peopleList2 = Arrays.asList(
                People.builder()
                        .age(12)
                        .name("xzx1")
                        .build(),
                People.builder()
                        .age(12)
                        .name("xzx1")
                        .build(),
                People.builder()
                        .age(13)
                        .name("xzx")
                        .build()
        );

        System.out.println(Optional.ofNullable(peopleList2).orElse(Lists.newArrayList()).stream().collect(
                Collectors.groupingBy(People::getAge,Collectors.mapping(People::getName,Collectors.toList()))
        ));




        //3 apple, 2 banana, others 1
        /*List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");
        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );
        System.out.println(result);


        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );
        Map<String, Long> counting = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.counting()));
        System.out.println(counting);
        Map<String, Integer> sum = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
        System.out.println(sum);


        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );
        //group by price
        Map<BigDecimal, List<Item>> groupByPriceMap =
                items.stream().collect(Collectors.groupingBy(Item::getPrice));
        System.out.println(groupByPriceMap);
        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> result =
                items.stream().collect(
                        Collectors.groupingBy(Item::getPrice,
                                Collectors.mapping(Item::getName, Collectors.toSet())
                        )
                );
        System.out.println(result);*/

    }

    @Data
    @ToString(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class People implements Serializable {
        private static final long serialVersionUID = -1497473985145989472L;
        private String name;
        private Integer age;

    }
}
