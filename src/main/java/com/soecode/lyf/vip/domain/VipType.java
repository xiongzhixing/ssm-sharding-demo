package com.soecode.lyf.vip.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VipType{
    private String name;
    private Integer level;
    private String code;
}