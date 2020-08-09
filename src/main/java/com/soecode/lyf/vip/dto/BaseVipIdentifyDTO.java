package com.soecode.lyf.vip.dto;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.FastDateFormat;

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

    public String getStartTimeStr(){
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(startTime);
    }

    public String getEndTimeStr(){
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(endTime);
    }
}