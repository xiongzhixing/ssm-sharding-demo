package com.soecode.lyf.vip.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Data
@ToString(callSuper = true)
public class HeyTapVipIdentifyDTO extends BaseVipIdentifyDTO {
    //当前包含超级会员身份
    private Boolean isHeyTapVip;
}