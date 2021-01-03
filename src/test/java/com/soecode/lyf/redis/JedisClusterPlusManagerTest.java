package com.soecode.lyf.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.soecode.lyf.BaseTest;
import com.soecode.lyf.manage.JedisClusterManager;
import com.soecode.lyf.manage.JedisClusterPlusManager;
import lombok.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JedisClusterManager Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 29, 2020</pre>
 */
public class JedisClusterPlusManagerTest extends BaseTest {
    private static List<People> peopleList = Lists.newArrayList(
            People.builder()
                    .id(1)
                    .name("zs")
                    .build(),
            People.builder()
                    .id(2)
                    .name("ll")
                    .build(),
            People.builder()
                    .id(3)
                    .name("ww")
                    .build(),
            People.builder()
                    .id(4)
                    .name("zl")
                    .build(),
            People.builder()
                    .id(5)
                    .name("qq")
                    .build()
    );

    @Autowired
    private JedisClusterPlusManager jedisClusterManager;

    @Test
    public void testComputeByFuntionIfAbsent() throws Exception {
        this.jedisClusterManager.computeByFuntionIfAbsent("test:1","1",new TypeReference<People>(){},t -> {
            return People.builder()
                    .id(1)
                    .name("xzx")
                    .build();
        },7 * 24 * 60 * 60);

        this.jedisClusterManager.computeByFuntionIfAbsent("test:1","1",new TypeReference<People>(){},t -> {
            return People.builder()
                    .id(1)
                    .name("xzx")
                    .build();
        },7 * 24 * 60 * 60);
    }

    @Test
    public void testComputeBySupplierIfAbsent() throws Exception {
        System.out.println(
                JSON.toJSONString(
                    this.jedisClusterManager.computeBySupplierIfAbsent("test111",new TypeReference<List<People>>(){}, () -> {
                        return Arrays.asList(
                                    People.builder()
                                        .id(1)
                                        .name("zs")
                                        .build(),
                                    People.builder()
                                        .id(2)
                                        .name("ll")
                                        .build(),
                                    People.builder()
                                        .id(3)
                                        .name("ww")
                                        .build()
                                );
                        },7 * 24 * 60 * 60)
                )
        );
        System.out.println(
                JSON.toJSONString(
                        this.jedisClusterManager.computeBySupplierIfAbsent("test111",new TypeReference<List<People>>(){}, () -> {
                            return Arrays.asList(
                                    People.builder()
                                            .id(1)
                                            .name("zs")
                                            .build(),
                                    People.builder()
                                            .id(2)
                                            .name("ll")
                                            .build(),
                                    People.builder()
                                            .id(3)
                                            .name("ww")
                                            .build()
                            );
                        },7 * 24 * 60 * 60)
                )
        );
    }

    @Test
    public void testComputeHashByFunctionIfAbsent() throws Exception {
        System.out.println(
                JSON.toJSONString(
                        this.jedisClusterManager.computeHashByFunctionIfAbsent(
                                "people_hash",
                                Lists.newArrayList("1","2"),
                                new TypeReference<People>(){},
                                People::getId,
                                this::selectList,
                                7 * 24 * 60 * 60)
                )
        );

        System.out.println(
                JSON.toJSONString(
                        this.jedisClusterManager.computeHashByFunctionIfAbsent(
                                "people_hash",
                                Lists.newArrayList("1","2","3","4","6"),
                                new TypeReference<People>(){},
                                People::getId,
                                this::selectList,
                                7 * 24 * 60 * 60)
                )
        );
    }

    public List<People> selectList(List<String> idList){
        Map<Integer,People> peopleMap = peopleList.stream()
                  .collect(
                          Collectors.toMap(
                                  item -> item.getId(), Function.identity()
                          )
                  );
        return idList.stream().map(item -> peopleMap.get(Integer.valueOf(item))).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Data
    @ToString(callSuper = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class People{
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


} 
