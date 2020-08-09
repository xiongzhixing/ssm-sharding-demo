package com.soecode.lyf.vip.dto;

import java.util.*;

public enum VipTypeEnum {
    HEYTAP_NEW_VIP("HeyTap新享会员", "heytap_new_vip", 1),
    HEYTAP_VIP("HeyTap大会员", "heytap_vip", 10),
    HEYTAP_BOOK_VIP("HeyTap书城会员", "heytap_book_vip", 20),
    HEYTAP_GAME_VIP("HeyTap游戏会员", "heytap_game_vip", 20),
    HEYTAP_SUPER_VIP("HeyTap超级会员", "HEYTAP_SUPER_VIP", 30);

    private String name;
    private String code;
    private Integer level;

    VipTypeEnum(String name, String code, Integer level) {
        this.name = name;
        this.code = code;
        this.level = level;
    }

    /**
     * 如果更高級的會員兩個同時消耗，則返回一個列表
     * @param vipTypeEnum
     * @return
     */
    public static List<VipTypeEnum> getHigherVip(VipTypeEnum vipTypeEnum){
        if(vipTypeEnum == null){
            throw new RuntimeException("參數異常");
        }
        VipTypeEnum higherVipTypeEnum = Arrays.stream(VipTypeEnum.values()).filter(Objects::nonNull)
                .filter(temp -> vipTypeEnum.getLevel() < temp.getLevel())
                .sorted(Comparator.comparingInt(VipTypeEnum::getLevel)).findFirst().orElse(null);

        //已經是最高級會員
        if(higherVipTypeEnum == null){
            return null;
        }

        List<VipTypeEnum> vipTypeEnumList = new ArrayList<>();
        for(VipTypeEnum temp:VipTypeEnum.values()){
            if(temp.getLevel().intValue() == higherVipTypeEnum.getLevel().intValue()){
                vipTypeEnumList.add(temp);
            }
        }

        return vipTypeEnumList;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Integer getLevel() {
        return level;
    }


}
