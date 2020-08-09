package com.soecode.lyf.vip.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Data
@ToString(callSuper = true)
public class BaseVipIdentifyDTO {

    private String code;
    private Date startTime;
    private Date endTime;

}