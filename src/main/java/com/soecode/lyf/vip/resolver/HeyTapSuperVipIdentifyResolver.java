package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapSuperVipIdentifyDTO;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class HeyTapSuperVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapSuperVipIdentifyDTO>{
    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_SUPER_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapSuperVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapSuperVip(isVipFlag);
    }

    @Override
    public void setUserIdentify(UserIdentifyDTO totalUserIdentifyDTO, HeyTapSuperVipIdentifyDTO heyTapSuperVipIdentifyDTO) {
        totalUserIdentifyDTO.setHeyTapSuperVipIdentifyDTO(heyTapSuperVipIdentifyDTO);
    }

}