package com.soecode.lyf.vip;

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
