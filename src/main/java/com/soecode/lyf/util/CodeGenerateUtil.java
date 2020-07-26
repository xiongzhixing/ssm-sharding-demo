package com.soecode.lyf.util;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerateUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerateUtil.class);
    private final static String base32Code = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final static int bitNum = 12;

    private static long MAX_BATCH_ID = 0xffff;

    private static long MAX_SEQ_NUM = 0xffffff;

    private static long MAX_RANDOM_NUM = 0xffff;

    private static long CHECK_BIT = 0xf;

    private static long BIT_5 = 0x1f;

    private static long PWD = 123456789l;

    public static Result checkout(String code){
        if(StringUtils.isBlank(code) || code.length() != 12){
            throw new IllegalArgumentException();
        }
        Result result = new Result();
        long num = convertStrToNum(code) ^ PWD;
        long checkout = num & CHECK_BIT;
        long random = (num >> 4) & MAX_RANDOM_NUM;
        long seqNum = ((num >> 20) & MAX_SEQ_NUM) ^ random;
        long batchId = ((num >> 44) & MAX_BATCH_ID) ^ random;

        if((batchId + seqNum + random) % (1 << 4) == checkout){
            result.setPass(true);
            result.setBatchId(batchId);
            result.setSeqNum(seqNum);
            result.setRandom(random);
        }

        return result;
    }



    public static String generateCode(long batchId,long seqNum){
        if(batchId > MAX_BATCH_ID || seqNum > MAX_SEQ_NUM){
            logger.warn("CodeGenerateUtil#generateCode illegal param batchId={},seqNum={}",batchId,seqNum);
            throw new IllegalArgumentException();
        }
        long random = ThreadLocalRandom.current().nextLong(MAX_RANDOM_NUM);
        long checkCode = (batchId + seqNum + random) % (1 << 4);
        //异或运算，防止生成的code字符重复；
        batchId = batchId ^ random;
        seqNum = seqNum ^ random;

        long num = (batchId << 24) | seqNum;
        num = (num << 16) | random;
        num = (num << 4) | checkCode;

        num = num ^ PWD;
        return convert32toStr(num);
    }

    private static long convertStrToNum(String code){
        long num = 0;
        for(int i = 0;i < code.length();i++){
            num = (num << 5) +  base32Code.indexOf(code.charAt(i));
        }
        return num;
    }

    private static String convert32toStr(long num){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < 12;i++){
            stringBuilder.append(base32Code.charAt((int)(num & BIT_5)));
            num = num >> 5;
        }
        return StringUtils.reverse(stringBuilder.toString());
    }

    public static void main(String[] args) {
        String code = CodeGenerateUtil.generateCode(10000L,99);
        System.out.println("code:" + code);
        System.out.println(CodeGenerateUtil.checkout(code));
    }

    @Data
    @ToString(callSuper = true)
    static class Result implements Serializable{
        private static final long serialVersionUID = -6120287431300945L;
        private boolean pass;
        private Long batchId;
        private Long seqNum;
        private Long random;
    }
}
