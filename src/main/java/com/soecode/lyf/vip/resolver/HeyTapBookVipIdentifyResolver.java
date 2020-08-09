package com.soecode.lyf.vip.resolver;

import com.soecode.lyf.vip.AbstractVipIdentifyResolver;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.HeyTapBookVipIdentifyDTO;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class HeyTapBookVipIdentifyResolver extends AbstractVipIdentifyResolver<HeyTapBookVipIdentifyDTO>{
    @Override
    public boolean isVipType(VipTypeEnum vipTypeEnum) {
        return VipTypeEnum.HEYTAP_BOOK_VIP == vipTypeEnum;
    }

    @Override
    public VipTypeEnum getVipType() {
        return VipTypeEnum.HEYTAP_BOOK_VIP;
    }

    @Override
    protected void setVipFlag(HeyTapBookVipIdentifyDTO userIdentifyDTO, boolean isVipFlag) {
        userIdentifyDTO.setIsHeyTapBookVip(isVipFlag);
    }

    @Override
    public void setUserIdentify(UserIdentifyDTO totalUserIdentifyDTO, HeyTapBookVipIdentifyDTO heyTapBookVipIdentifyDTO) {
        totalUserIdentifyDTO.setHeyTapBookVipIdentifyDTO(heyTapBookVipIdentifyDTO);
    }

}