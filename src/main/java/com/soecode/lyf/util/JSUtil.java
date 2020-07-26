package com.soecode.lyf.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSUtil {
    private Map<String,JSONObject> jsonObjsMap;
    private Map<String,Object> keyAndValue;
    private String filePath;
    private StringBuilder stringBuilder;

    //private int modifyNum;

    private void readJS(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.stringBuilder = new StringBuilder();

        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException("file not exist");
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line = null;
            while((line = br.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            //System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJS(){
        jsonObjsMap = new LinkedHashMap<>();
        //Pattern pattern = Pattern.compile("(\\s*|\t)var\\s+\\w+\\s*=\\s*\\{.*\\1}(,|;)?");
        Pattern pattern = Pattern.compile("(var +\\w+ *= *)(\\{((?!\\};).|\n)*(\t|\\s*)\\});\\s*\n");
        Matcher matcher = pattern.matcher(stringBuilder.toString());
        while(matcher.find()){
            //System.out.println(matcher.group());
            jsonObjsMap.put(matcher.group(1),new JSONObject(JSON.parseObject(matcher.group(2),LinkedHashMap.class, Feature.OrderedField)));
        }
        //System.out.println(jsonObjsMap);
        keyAndValue = new LinkedHashMap<>();
        for(Map.Entry<String,JSONObject> entry: jsonObjsMap.entrySet()){
            parseKeyAndValByVar(keyAndValue,"",entry.getValue());
        }

        System.out.println(JSON.toJSONString(keyAndValue));
    }

    private static void parseKeyAndValByVar(Map<String,Object> keyAndValue,String codeId,Object object){
        if(object == null){
            return;
        }
        if(object instanceof JSONObject){
            for(Map.Entry<String,Object> entry: ((JSONObject)object).entrySet()){
                Object obj = entry.getValue();
                String oneCodeId = (StringUtils.isEmpty(codeId) ? "" : codeId + "-") + entry.getKey();
                setValue(keyAndValue, obj, oneCodeId);
            }
        }else if(object instanceof JSONArray){
            for(Object obj:((JSONArray)object)){
                String oneCodeId = codeId;
                setValue(keyAndValue, obj, oneCodeId);
            }
        }
    }

    private static void setValue(Map<String, Object> keyAndValue, Object obj, String oneCodeId) {
        if(obj instanceof String){
            if(keyAndValue.get(oneCodeId) == null){
                keyAndValue.put(oneCodeId,obj);
            }else if(keyAndValue.get(oneCodeId) instanceof String){
                keyAndValue.put(oneCodeId, getList((String)keyAndValue.get(oneCodeId),(String)obj));
            }else if(keyAndValue.get(oneCodeId) instanceof List){
                ((List) keyAndValue.get(oneCodeId)).add(obj);
            }
        }else if(obj instanceof JSONObject || obj instanceof JSONArray){
            parseKeyAndValByVar(keyAndValue,oneCodeId,obj);
        }
    }

    private static <T> List<T> getList(T...ts){
        List<T> list = new ArrayList<>();
        if(ts == null){
            return list;
        }
        for(T t:ts){
            list.add(t);
        }
        return list;
    }

    public JSUtil(String filePath){
        try {
            readJS(filePath);
            parseJS();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int modifyVal(List<ItemChange> items){
        int modifyNum = 0;
        if(items == null){
            return modifyNum;
        }
        //System.out.println(jsonObjsMap);
        for(ItemChange item:items){
            String codeId = item.getCodeId();
            for(JSONObject json:jsonObjsMap.values()){
                modifyNum += toModify(item,codeId, json);
            }
        }
        //System.out.println(jsonObjsMap);
        replaceFile();
        return modifyNum;
    }

    public int delVal(List<ItemChange> items){
        int delNum = 0;
        if(items == null){
            return delNum;
        }
        for(ItemChange item:items){
            for(Map.Entry<String,JSONObject> entry:jsonObjsMap.entrySet()){
                delNum += toDelVal(item,entry.getValue(),item.getCodeId());
            }
        }
        replaceFile();
        return delNum;
    }

    private int toDelVal(ItemChange item,JSONObject json,String codeId){
        int delNum = 0;
        if(!codeId.contains("-")){
            if(item.getOldVal().equals(json.getString(codeId))){
                json.remove(codeId);
                delNum++;
            }
            return delNum;
        }else {
            String preCodeId = codeId.substring(0, codeId.indexOf("-"));
            String sufCodeId = codeId.replaceFirst(preCodeId + "-", "");

            Object obj = json.get(preCodeId);
            if (StringUtils.isEmpty(sufCodeId)) {
                throw new RuntimeException("codeId format isn't standard");
            }
            if (obj == null) {
                return delNum;
            }
            delNum += toDelVal(item,(JSONObject) obj,sufCodeId);
            if(((JSONObject) obj).size() == 0){
                json.remove(preCodeId);
            }
            return delNum;
        }
    }

    private void replaceFile(){
        System.out.println(this.stringBuilder.toString());
        String template = this.stringBuilder.toString();
        for(Map.Entry<String,JSONObject> entry:jsonObjsMap.entrySet()){
            Pattern pattern = Pattern.compile("((?!\n)\\s|\t)*" + entry.getKey() + "(\\{((?!\\};).|\n|\t|\r)*\\});");
            Matcher matcher = pattern.matcher(template);
            matcher.find();
            template = template.replace(matcher.group(2),JSON.toJSONString(entry.getValue(), SerializerFeature.PrettyFormat).replaceAll("\n","\n" + matcher.group(1)));
        }
        this.stringBuilder = new StringBuilder(template);
        System.out.println(this.stringBuilder.toString());

        writeFile();
    }

    private void writeFile(){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(this.filePath));
            pw.print(this.stringBuilder);
            //System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            pw.close();
        }
    }

    private int toModify(ItemChange item,String codeId,JSONObject json){
        int modifyNum = 0;
        if(!codeId.contains("-")){
            if(item.getOldVal().equals(json.getString(codeId))){
                json.put(codeId,item.modifyVal);
                modifyNum++;
            }
            return modifyNum;
        }else {
            String preCodeId = codeId.substring(0, codeId.indexOf("-"));
            String sufCodeId = codeId.replaceFirst(preCodeId + "-", "");

            Object obj = json.get(preCodeId);
            if (StringUtils.isEmpty(sufCodeId)) {
                throw new RuntimeException("codeId format isn't standard");
            }
            if (obj == null) {
                return modifyNum;
            }
            return toModify(item,sufCodeId,(JSONObject) obj) + modifyNum;
        }
    }

    public static void main(String[] args) {
        JSUtil jsUtil = new JSUtil("C:\\Users\\Administrator\\Desktop\\1.js");


        System.out.println(jsUtil.keyAndValue);

        /*List<ItemChange> itemChanges = new ArrayList<>();
        itemChanges.add(new ItemChange("common_a-asd-zx","","modify-1","asd"));
        itemChanges.add(new ItemChange("common_a-wsd","","modify-2","asd"));
        itemChanges.add(new ItemChange("common_b","","modify-3","asd"));
        itemChanges.add(new ItemChange("result_a-abc","","modify-4","asd"));

        itemChanges.add(new ItemChange("test_a-a","","modify-5","asd"));
        itemChanges.add(new ItemChange("test_b","","modify-6","asd"));
        itemChanges.add(new ItemChange("test_c-c_3-d_1","","modify-7","asd"));
        itemChanges.add(new ItemChange("test_c-c_2","","modify-8","asd"));

        System.out.println(jsUtil.modifyVal(itemChanges));*/

        //List<ItemChange> itemChanges = new ArrayList<>();
        /*itemChanges.add(new ItemChange("common_a-asd-zx","","","asd"));
        itemChanges.add(new ItemChange("common_b","","","asd"));
        itemChanges.add(new ItemChange("test_c-c_3-d_1","","","asd"));*/
        //itemChanges.add(new ItemChange("test_d-d_1-d_1_1-d_1_1_1-d_1_1_1_1","","","asd"));

        //System.out.println(jsUtil.delVal(itemChanges));





        /*String template = "default([],function(){\n\tvar common = {\n\t\t\"common_a\":{\n\t\t\t\"asd\":{\n\t\t\t\t\"zx\":\"asd\",\n\t\t\t\t\"sdf\":\"wsad\"\n\t\t\t},\n\t\t\t\"wsd\":\"wsdc\"\n\t\t},\n\t\t\"common_b\":\"qsas\",\n\t\t\"common_c\":\"ewq\"\n\t};\n\t\n\tvar result = {\n\t\t\"result_a\":{\n\t\t\t\"abc\":\"ert\",\n\t\t\t\"a_2\":\"wsx\"\n\t\t},\n\t\t\"result_b\":\"wsad\"\n    };    \n\t\n\t\n})\n";
        String regex = "var common = (\\{((?!\\};).|\n|\t|\n)*\\});";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        matcher.group();*/

        Map<String,Object> map = new LinkedHashMap<>();
        JSUtil.parseKeyAndValByVar(map,"",JSON.parseObject("{\n" + "    \"guanBiAnNiuButton\":{\n" + "        \"id\":\"guanBiAnNiuButton\",\n" + "        \"image\":{\n" + "            \"value\":\"https://icredits-img.oss-cn-beijing.aliyuncs.com/vip/584aa760ea9c37fa16e7c5447cd11b3a.png\"\n" + "        },\n" + "        \"label\":\"关闭按钮\",\n" + "        \"name\":\"关闭按钮\",\n" + "        \"value\":\"关闭按钮\"\n" + "    },\n" + "    \"tuPianImage\":{\n" + "        \"id\":\"tuPianImage\",\n" + "        \"link\":{\n" + "            \"defaultLinkDetail\":[\n" + "            ],\n" + "            \"defaultLinkType\":\"\",\n" + "            \"downloadUrl\":\"\",\n" + "            \"linkDetail\":[\n" + "                {\n" + "                    \"appVersion\":\"\",\n" + "                    \"linkType\":\"BROWSER\",\n" + "                    \"linkUrl\":\"http://baiduc.om\",\n" + "                    \"osVersion\":\"\",\n" + "                    \"packageName\":\"\",\n" + "                    \"packageType\":\"\"\n" + "                }],\n" + "            \"linkType\":\"BROWSER\"\n" + "        },\n" + "        \"name\":\"图片\",\n" + "        \"value\":\"https://icredits-img.oss-cn-beijing.aliyuncs.com/vip/4d65a793efdbda449913418f76904008.jpg\"\n" + "    }\n" + "}"));
        System.out.println(map);





    }

    static class ItemChange{
        private String codeId;
        private String addVal;
        private String modifyVal;
        private String oldVal;

        public ItemChange(String codeId, String addVal, String modifyVal, String oldVal) {
            this.codeId = codeId;
            this.addVal = addVal;
            this.modifyVal = modifyVal;
            this.oldVal = oldVal;
        }

        public String getCodeId() {
            return codeId;
        }

        public void setCodeId(String codeId) {
            this.codeId = codeId;
        }

        public String getAddVal() {
            return addVal;
        }

        public void setAddVal(String addVal) {
            this.addVal = addVal;
        }

        public String getModifyVal() {
            return modifyVal;
        }

        public void setModifyVal(String modifyVal) {
            this.modifyVal = modifyVal;
        }

        public String getOldVal() {
            return oldVal;
        }

        public void setOldVal(String oldVal) {
            this.oldVal = oldVal;
        }
    }
}