package com.soecode.lyf.util;

import lombok.*;
import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Administrator
 */
public class CopyUtil {
    private static final DozerBeanMapper mapper = new DozerBeanMapper();

    public static <U,V> V  copyObject(U source, Class<V> tarClas){
        return source == null ? null : mapper.map(source,tarClas);
    }

    public static <U,V> List<V>  copyList(List<U> sourceList, Class<V> tarClas){
        List<V> result = new ArrayList<>();
        Optional.ofNullable(sourceList).orElse(new ArrayList<>()).stream().filter(Objects::nonNull).forEach(obj -> {
            result.add(copyObject(obj,tarClas));
        });
        return result;
    }

    public static void main(String[] args) {
        People people = new People();
        people.setAge(100L);
        System.out.println(CopyUtil.copyObject(people,People1.class));

        List<People> peopleList = new ArrayList<>();
        peopleList.add(People.builder().age(100L).build());
        peopleList.add(People.builder().age(200L).build());

        System.out.println(CopyUtil.copyList(peopleList,People1.class));


    }

    @Data
    @ToString(callSuper = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class People{
        private Long age;
    }

    @Data
    @ToString(callSuper = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class People1{
        private Integer age;
    }

}
