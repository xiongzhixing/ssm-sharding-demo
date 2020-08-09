package com.soecode.lyf.vip.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VipUserStatus{
    private Integer id;
    private String code;
    private Date expireDate;
}