package com.soecode.lyf.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.soecode.lyf.BaseTest;
import com.soecode.lyf.manage.JedisClusterManager;
import lombok.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * JedisClusterManager Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 29, 2020</pre>
 */
public class JedisClusterManagerTest extends BaseTest {
    @Autowired
    private JedisClusterManager jedisClusterManager;

    @Test
    public void testComputeByFuntionIfAbsent() throws Exception {
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
