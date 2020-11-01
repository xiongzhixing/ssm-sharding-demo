package com.soecode.lyf;

import lombok.*;

import java.io.Serializable;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/3/22 0022
 **/
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class People implements Serializable {
    private static final long serialVersionUID = -8901185747477348522L;

    private Integer age;
    private String name;
}