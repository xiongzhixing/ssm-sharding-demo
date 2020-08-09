package com.soecode.lyf.vip;

import com.soecode.lyf.vip.domain.VipUserStatus;
import com.soecode.lyf.vip.dto.BaseVipIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractVipIdentifyResolver<T extends BaseVipIdentifyDTO> {

    public abstract boolean isVipType(VipTypeEnum vipTypeEnum);

    public abstract VipTypeEnum getVipType();

    protected abstract void setVipFlag(T userIdentifyDTO, boolean isVipFlag);

    public T queryVipIdentify(List<VipUserStatus> vipUserStatusList,Class<T> cls) throws IllegalAccessException, InstantiationException {
        if(CollectionUtils.isEmpty(vipUserStatusList)){
            return null;
        }

        VipUserStatus vipUserStatus = vipUserStatusList.stream().filter(Objects::nonNull)
                .filter(temp -> getVipType().getCode().equals(temp.getCode()))
                .findFirst().orElse(null);

        if(vipUserStatus == null || vipUserStatus.getExpireDate() == null){
            return null;
        }

        T userIdentifyDTO = cls.newInstance();

        userIdentifyDTO.setCode(getVipType().getCode());
        userIdentifyDTO.setEndTime(vipUserStatus.getExpireDate());

        //查询消耗开始时间，默认当前时间
        Date now = new Date();
        Date startTime = now;
        List<VipTypeEnum> vipTypeEnumList = VipTypeEnum.getHigherVip(getVipType());
        if(CollectionUtils.isNotEmpty(vipTypeEnumList)){
            //存在更高级会员，
            List<String> higherCodeList = vipTypeEnumList.stream().map(VipTypeEnum::getCode).collect(Collectors.toList());
            VipUserStatus higherVipLatestVipUserStatus = vipUserStatusList.stream().filter(Objects::nonNull).filter(temp -> higherCodeList.contains(temp.getCode()))
                    .sorted((v1,v2) -> {
                        if(v1.getExpireDate() == null){
                            return 1;
                        }
                        if(v2.getExpireDate() == null){
                            return -1;
                        }
                        return (int)(v2.getExpireDate().getTime() - v1.getExpireDate().getTime());
                    }).findFirst().orElse(null);

            if(higherVipLatestVipUserStatus != null && higherVipLatestVipUserStatus.getExpireDate()  != null){
                startTime = higherVipLatestVipUserStatus.getExpireDate();
            }
        }

        userIdentifyDTO.setStartTime(startTime);
        if(now.getTime() >= userIdentifyDTO.getStartTime().getTime() &&
                now.getTime() <= userIdentifyDTO.getEndTime().getTime()){
            setVipFlag(userIdentifyDTO,true);
        }

        return userIdentifyDTO;
    }

}
