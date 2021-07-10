package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

public class RandomTree {
    private static int seqNo = 1;
    private static int NUM_MODEL = 5;

    public static void main(String[] args) {
        Node root = Node.builder().seqNo(seqNo++).build();
        buildTree(root,5);

        System.out.println(JSON.toJSONString(root));

    }

    public static void buildTree(Node root,int deepth){
        buildNode(root,(int)(Math.random() * NUM_MODEL + 1),1,deepth);
    }

    public static void buildNode(Node fatherNode,int subNum,int currentDeepth,int deepth){
        if(currentDeepth > deepth){
            return;
        }
        List<Node> subNodeList = Lists.newArrayList();
        fatherNode.setSubNode(subNodeList);
        for(int i = 0;i < subNum;i++){
            Node subNode = Node.builder().seqNo(seqNo++).build();
            subNodeList.add(subNode);
        }

        for(Node node:fatherNode.getSubNode()){
            buildNode(node,(int)(Math.random() * NUM_MODEL + 1),currentDeepth + 1,deepth);
        }
    }

    @Data
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Node{
        private Integer seqNo;
        private List<Node> subNode;
    }
}
