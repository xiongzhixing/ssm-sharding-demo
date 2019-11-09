package com.soecode.lyf.http;

import java.io.Serializable;

public class PeopleHttpRequestInvoker extends HttpBaseRequest<PeopleHttpRequestInvoker.PeopelRequest,PeopleHttpRequestInvoker.PeopleResponse,PeopleHttpRequestInvoker.PeopleHttpResponse> {


    @Override
    public InvokerEnum getInvokerEnum() {
        return InvokerEnum.GET;
    }

    @Override
    public String getUrl(PeopelRequest peopelRequest) {
        return null;
    }

    @Override
    public String getParamMap(PeopelRequest peopelRequest) {
        return null;
    }

    @Override
    public String getParamJson(PeopelRequest peopelRequest) {
        return null;
    }

    @Override
    public PeopleResponse buildContent(PeopleHttpResponse peopleHttpResponse) {
        return null;
    }

    public static void main(String[] args) {
        PeopleHttpRequestInvoker peopleHttpRequestInvoker = new PeopleHttpRequestInvoker();
        peopleHttpRequestInvoker.invoker(new PeopelRequest());
    }

    static class PeopelRequest implements Serializable{
        private String name;
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    static class PeopleHttpResponse implements Serializable{
        private String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    static class PeopleResponse implements Serializable{
        private String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
