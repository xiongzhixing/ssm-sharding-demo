package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestLamada111 {
    public static void main(String[] args) {
        Map<String, List<String>> mapList = new HashMap<>();


        mapList.put("zs", Lists.newArrayList("java","c","php"));
        mapList.put("ls", Lists.newArrayList("ps","c++","c#"));
        mapList.put("ww", Lists.newArrayList("java","php","ps"));
        mapList.put("zl", Lists.newArrayList("java","c","python"));


        Map<String,List<String>> mapListNew = new HashMap<>();
        Optional.ofNullable(mapList).orElse(new HashMap<>()).entrySet()
                .stream()
                .forEach(item -> {
                    Optional.ofNullable(item.getValue()).orElse(Lists.newArrayList())
                            .stream()
                            .forEach(value -> {
                                if(mapListNew.get(value) == null){
                                    mapListNew.put(value,Lists.newArrayList());
                                }
                                mapListNew.get(value).add(item.getKey());
                            });
                });
        System.out.println(
                JSON.toJSONString(
                        mapListNew
                )
        );
    }
}
