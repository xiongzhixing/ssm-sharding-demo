package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapSuperVipIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
public class HeyTapSuperVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapSuperVipIdentifyDTO>{
    @Override
    public boolean isVipType(VipTypeEnum vipTypeEnum) {
        return VipTypeEnum.HEYTAP_SUPER_VIP == vipTypeEnum;
    }

    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_SUPER_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapSuperVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapSuperVip(isVipFlag);
    }

}