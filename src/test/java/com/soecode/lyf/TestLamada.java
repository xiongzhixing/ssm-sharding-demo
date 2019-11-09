package com.soecode.lyf;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;
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

    }

    @Data
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    static class People implements Serializable {
        private static final long serialVersionUID = -1497473985145989472L;
        private String name;
        private Integer age;

    }
}
