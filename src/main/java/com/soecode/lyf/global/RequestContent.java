package com.soecode.lyf.global;

import java.util.HashMap;

public class RequestContent extends HashMap<String,String> {
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";

    public Integer getUserId(){
        return Integer.valueOf(this.get(USER_ID));
    }

    public void setUserId(Integer userId){
        this.put(USER_ID,String.valueOf(userId));   
    }

    public String getUserName(){
        return this.get(USER_NAME);
    }

    public void setUserName(String userName){
        this.put(USER_NAME,userName);
    }

}
