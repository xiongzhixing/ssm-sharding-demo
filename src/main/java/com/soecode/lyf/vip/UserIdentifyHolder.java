package com.soecode.lyf.vip;

import com.google.common.collect.Lists;
import com.soecode.lyf.vip.domain.VipType;
import com.soecode.lyf.vip.domain.VipUserStatus;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class UserIdentifyHolder {
    @Autowired
    private List<AbstractVipIdentifyResolver> vipIdentifyResolverList;

    public UserIdentifyDTO queryUserIdentify(List<VipUserStatus> vipUserStatusList){
        UserIdentifyDTO userIdentifyDTO = new UserIdentifyDTO();
        for(AbstractVipIdentifyResolver resolver:vipIdentifyResolverList){
            resolver.getClass().getSuperclass().getA
        }

    }




    private static List<VipType> vipTypeList;

    private static List<VipUserStatus> vipUserStatusList = null;

    static {
        vipTypeList = Lists.newArrayList(
                new VipType(VipTypeEnum.HEYTAP_NEW_VIP.getName(),
                        VipTypeEnum.HEYTAP_NEW_VIP.getLevel(),
                        VipTypeEnum.HEYTAP_NEW_VIP.getCode()),
                new VipType(VipTypeEnum.HEYTAP_VIP.getName(),
                        VipTypeEnum.HEYTAP_VIP.getLevel(),
                        VipTypeEnum.HEYTAP_VIP.getCode()),
                new VipType(VipTypeEnum.HEYTAP_BOOK_VIP.getName(),
                        VipTypeEnum.HEYTAP_BOOK_VIP.getLevel(),
                        VipTypeEnum.HEYTAP_BOOK_VIP.getCode()),
                new VipType(VipTypeEnum.HEYTAP_SUPER_VIP.getName(),
                        VipTypeEnum.HEYTAP_SUPER_VIP.getLevel(),
                        VipTypeEnum.HEYTAP_SUPER_VIP.getCode())
        );

        try {
            vipUserStatusList = Lists.newArrayList(
                    new VipUserStatus(1,
                            VipTypeEnum.HEYTAP_SUPER_VIP.getCode(),
                            FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-08-20 12:00:00")),
                    new VipUserStatus(2,
                            VipTypeEnum.HEYTAP_GAME_VIP.getCode(),
                            FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-09-20 12:00:00")),
                    new VipUserStatus(3,
                            VipTypeEnum.HEYTAP_BOOK_VIP.getCode(),
                            FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-10-20 12:00:00")),
                    new VipUserStatus(4,
                            VipTypeEnum.HEYTAP_VIP.getCode(),
                            FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2020-12-20 12:00:00")),
                    new VipUserStatus(5,
                            VipTypeEnum.HEYTAP_NEW_VIP.getCode(),
                            FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2021-02-20 12:00:00"))
                    );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }










}