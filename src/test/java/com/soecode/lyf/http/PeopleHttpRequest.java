package com.soecode.lyf.http;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.http.dto.BaseResult;
import com.soecode.lyf.http.dto.PeopleRequest;
import com.soecode.lyf.http.dto.PeopleResponse;

/**
 * @author 熊志星
 * @description
 * @date 2019/10/17
 */
public class PeopleHttpRequest extends BaseHttpRequest<PeopleRequest, PeopleResponse, BaseResult<PeopleResponse>> {


    @Override
    public String getUrl(PeopleRequest peopleRequest) {
        return "";
    }

    @Override
    public String getHeader(PeopleRequest peopleRequest) {
        return JSON.toJSONString(peopleRequest);
    }

    @Override
    public boolean isSuccess(BaseResult<PeopleResponse> peopleRequestBaseResult) {
        if(peopleRequestBaseResult != null && peopleRequestBaseResult.getCode() == 200 && peopleRequestBaseResult.getData() != null){
            return true;
        }
        return false;
    }

    @Override
    public InvokerEnum getInvoker() {
        return InvokerEnum.GET;
    }

    @Override
    public PeopleResponse builderResponse(BaseResult<PeopleResponse> peopleRequestBaseResult) {
        return peopleRequestBaseResult.getData();
    }

    public static void main(String[] args) {
        PeopleHttpRequest peopleHttpRequest = new PeopleHttpRequest();
        PeopleResponse peopleResponse = peopleHttpRequest.invoker();
        System.out.println(JSON.toJSONString(peopleResponse));
    }
}