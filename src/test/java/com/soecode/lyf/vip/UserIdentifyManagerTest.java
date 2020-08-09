package com.soecode.lyf.vip;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.soecode.lyf.BaseTest;
import com.soecode.lyf.dto.AppointExecution;
import com.soecode.lyf.vip.domain.VipType;
import com.soecode.lyf.vip.domain.VipUserStatus;
import com.soecode.lyf.vip.dto.VipTypeEnum;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
public class UserIdentifyManagerTest extends BaseTest {
    @Autowired
    private UserIdentifyManager userIdentifyManager;

    @Test
    public void testAppoint() throws Exception {
        try {
            List<VipUserStatus> vipUserStatusList = Lists.newArrayList(
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

            System.out.println(JSON.toJSONString(this.userIdentifyManager.queryUserIdentify(vipUserStatusList)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}