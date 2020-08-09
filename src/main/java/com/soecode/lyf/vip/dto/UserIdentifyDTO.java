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
public class UserIdentifyDTO {
    private HeyTapNewVipIdentifyDTO heyTapNewVipIdentifyDTO;
    private HeyTapVipIdentifyDTO heyTapVipIdentifyDTO;
    private HeyTapBookVipIdentifyDTO heyTapBookVipIdentifyDTO;
    private HeyTapGameVipIdentifyDTO heyTapGameVipIdentifyDTO;
    private HeyTapSuperVipIdentifyDTO heyTapSuperVipIdentifyDTO;

}