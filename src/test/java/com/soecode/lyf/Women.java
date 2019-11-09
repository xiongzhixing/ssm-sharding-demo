package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import lombok.*;

/**
 * @author 熊志星
 * @description
 * @date 2019/9/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Women extends People {
    private Integer age;

    public static void main(String[] args) {
        Women women = new Women();
        women.setAge(12);
        women.setName("xzx");

        System.out.println(JSON.toJSONString(women));
    }
}