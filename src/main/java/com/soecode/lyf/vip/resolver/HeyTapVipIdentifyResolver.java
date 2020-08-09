package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapVipIdentifyDTO;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class HeyTapVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapVipIdentifyDTO>{
    @Override
    public boolean isVipType(VipTypeEnum vipTypeEnum) {
        return VipTypeEnum.HEYTAP_VIP == vipTypeEnum;
    }

    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapVip(isVipFlag);
    }

    @Override
    public void setUserIdentify(UserIdentifyDTO totalUserIdentifyDTO, HeyTapVipIdentifyDTO heyTapVipIdentifyDTO) {
        totalUserIdentifyDTO.setHeyTapVipIdentifyDTO(heyTapVipIdentifyDTO);
    }
}