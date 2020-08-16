package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapNewVipIdentifyDTO;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class HeyTapNewVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapNewVipIdentifyDTO>{
    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_NEW_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapNewVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapNewVip(isVipFlag);
    }

    @Override
    public void setUserIdentify(UserIdentifyDTO totalUserIdentifyDTO, HeyTapNewVipIdentifyDTO heyTapNewVipIdentifyDTO) {
        totalUserIdentifyDTO.setHeyTapNewVipIdentifyDTO(heyTapNewVipIdentifyDTO);
    }
}