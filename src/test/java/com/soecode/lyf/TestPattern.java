package com.soecode.lyf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestPattern {
    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200));
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String relativelyPath=System.getProperty("user.dir");
        Integer batchId = 1000;
        List<String> list = generate(1000);
        int batchNum = (int)Math.ceil(list.size() * 1.0 / threadPoolExecutor.getCorePoolSize());

        //清空目录下文件
        File file = new File(relativelyPath + File.separatorChar + batchId);
        if(!file.exists()){
           file.mkdir();
        }else{
            for(File tempFile:file.listFiles()){
                if(tempFile.isFile()){
                    tempFile.delete();
                }else{
                    FileUtils.deleteDirectory(tempFile);
                }
            }
        }

        List<Future<String>> futureList = new ArrayList<>();
        int startIdx = 0;
        for(int i = 0;i < threadPoolExecutor.getCorePoolSize();i++){
            int endIdx = (startIdx + batchNum) > list.size() ? list.size() : (startIdx + batchNum);
            futureList.add(threadPoolExecutor.submit(new MyTask(startIdx,endIdx,100,list,relativelyPath + File.separatorChar + batchId + File.separatorChar + i)));
            startIdx = endIdx;
        }
        //合并文件
        List<String> filePathList = new ArrayList<>();
        for(Future<String> future:futureList){
            filePathList.add(future.get());
        }

        File finalFile = new File(relativelyPath + File.separatorChar + batchId + File.separatorChar + batchId + ".txt");
        for(String subFilePath:filePathList){
            File subFile = new File(subFilePath);
            String content = FileUtils.readFileToString(subFile,"UTF-8");
            FileUtils.write(finalFile,content + "\n","UTF-8",true);

            subFile.delete();
        }
    }

    private static List<String> generate(int num){
        List<String> list = new ArrayList<>(num);
        for(int i=0;i < num;i++){
            list.add(generateString());
        }
        return list;
    }

    private static  String generateString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < ((int)(Math.random() * 20) + 5);i++){
            stringBuilder.append(generateChar());
        }
        return stringBuilder.toString();
    }

    private static char generateChar(){
        int temp = (int)(Math.random() * 26);
        return (char)(temp + (temp % 2 == 0 ? 65 : 97));
    }

    static class MyTask implements Callable<String>{
        private int startIdx;
        private int endIdx;
        private int pageSize;
        private List<String> list;
        private String filePath;

        public MyTask(int startIdx, int endIdx, int pageSize, List<String> list,String filePath) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.pageSize = pageSize;
            this.list = list;
            this.filePath = filePath;
        }

        @Override
        public String call() throws Exception {
            File file = new File(filePath);
            int idx = startIdx;
            boolean isFirst = true;
            while(idx < endIdx){
                int tempIdx = (idx + pageSize) > endIdx ? endIdx : (idx + pageSize);
                List<String> subList = list.subList(startIdx,tempIdx);
                FileUtils.write(file, StringUtils.join(subList,"\n") + (!isFirst ? "\n" : ""), "UTF-8",true);
                isFirst = false;
                if((idx + pageSize) > endIdx){
                    break;
                }
                idx = tempIdx;
            }
            return filePath;
        }
    }
}
