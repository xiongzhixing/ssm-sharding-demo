package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.*;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestLamda1 {
    public static void main(String[] args) throws ParseException {
        List<People> peopleList = Lists.newArrayList(
                People.builder()
                        .status(StatusEnum.DISABLE.name())
                        .sortNum(5)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-01-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.DELETED.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-01-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.ENABLE.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-02-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.ENABLE.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-03-11 00:00:00"))
                        .build(),People.builder()
                        .status(StatusEnum.DELETED.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-01-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.DELETED.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-01-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.DELETED.name())
                        .sortNum(10)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-01-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.ENABLE.name())
                        .sortNum(20)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-02-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.DISABLE.name())
                        .sortNum(5)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-05-11 00:00:00"))
                        .build(),
                People.builder()
                        .status(StatusEnum.ENABLE.name())
                        .sortNum(1)
                        .updateTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2019-01-11 00:00:00"))
                        .build()
        );

        Collections.shuffle(peopleList);

        System.out.println(
                JSON.toJSONString(
                        peopleList.stream()
                                .sorted(
                                    Comparator.comparing(People::getStatus,Comparator.reverseOrder())
                                              .thenComparing(People::getSortNum,Comparator.reverseOrder())
                                              .thenComparing(People::getUpdateTime,Comparator.reverseOrder())
                                ).collect(Collectors.toList())
                )
        );

        System.out.println(
                JSON.toJSONString(
                        peopleList.stream()
                                .sorted((e1,e2) -> {
                                    if(!e1.getStatus().equals(e2.getStatus())){
                                        return e2.getStatus().compareTo(e1.getStatus());
                                    }

                                    if(e1.getSortNum().intValue() != e2.getSortNum().intValue()){
                                        return e2.getSortNum().intValue() - e1.getSortNum().intValue();
                                    }

                                    return e2.getUpdateTime().getTime() - e1.getUpdateTime().getTime() > 0 ? 1 : -1;
                                }).collect(Collectors.toList())
                )
        );



    }

    @Data
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class People{
        private String status;
        private Integer sortNum;
        private Date updateTime;
    }

    public enum StatusEnum{
        ENABLE,
        DISABLE,
        DELETED,
        ;
    }
}
