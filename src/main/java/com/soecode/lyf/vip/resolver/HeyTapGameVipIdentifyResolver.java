package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapGameVipIdentifyDTO;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class HeyTapGameVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapGameVipIdentifyDTO>{
    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_GAME_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapGameVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapGameVip(isVipFlag);
    }

    @Override
    public void setUserIdentify(UserIdentifyDTO totalUserIdentifyDTO, HeyTapGameVipIdentifyDTO heyTapGameVipIdentifyDTO) {
        totalUserIdentifyDTO.setHeyTapGameVipIdentifyDTO(heyTapGameVipIdentifyDTO);
    }
}