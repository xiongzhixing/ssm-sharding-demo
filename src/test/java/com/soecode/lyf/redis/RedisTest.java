package com.soecode.lyf.redis;

import com.soecode.lyf.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/3/22 0022
 **/
public class RedisTest extends BaseTest{
    @Autowired
    private JedisCluster jedisCluster;


    @Test
    public void test(){
        this.jedisCluster.set("name","xzx");
    }
}