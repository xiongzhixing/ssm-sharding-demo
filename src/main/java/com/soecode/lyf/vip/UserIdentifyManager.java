package com.soecode.lyf.vip;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.soecode.lyf.vip.domain.VipType;
import com.soecode.lyf.vip.domain.VipUserStatus;
import com.soecode.lyf.vip.dto.UserIdentifyDTO;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.List;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
@Component
public class UserIdentifyManager {
    @Autowired
    private List<AbstractVipIdentifyResolver> vipIdentifyResolverList;

    public UserIdentifyDTO queryUserIdentify(List<VipUserStatus> vipUserStatusList){
        UserIdentifyDTO userIdentifyDTO = new UserIdentifyDTO();
        for(AbstractVipIdentifyResolver resolver:vipIdentifyResolverList){
            try {
                resolver.queryVipIdentify(userIdentifyDTO,vipUserStatusList,resolver.getParamClass());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return userIdentifyDTO;
    }
}