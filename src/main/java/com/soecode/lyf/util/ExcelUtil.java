package com.soecode.lyf.util;

import com.google.common.collect.Maps;
import com.soecode.lyf.annotation.ExcelVOAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * Excel
 * @author 熊志星
 * @history 2019/3/18 新建
 * @since JDK1.7
 */
@Slf4j
public class ExcelUtil<T> {
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    private Class<T> clazz;
    private Map<Integer,PropertyDescriptor> propertyDescriptorMap;

    public ExcelUtil(Class<T> clazz) throws NoSuchFieldException {
        this.clazz = clazz;
        propertyDescriptorMap = Maps.newHashMap();

        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(clazz);
        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            String propertyName = propertyDescriptor.getName();
            log.debug("propertyName={}",propertyName);
            Field field = clazz.getDeclaredField(propertyName);
            if(field == null){
                continue;
            }
            // 将有注解的field存放到map中.
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                ExcelVOAttribute attr = field
                        .getAnnotation(ExcelVOAttribute.class);
                int col = getExcelCol(attr.column());// 获得列号
                propertyDescriptorMap.put(col, propertyDescriptor);
            }
        }
    }

    public Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (XLS.equalsIgnoreCase(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (XLSX.equalsIgnoreCase(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        }else{
            log.debug("can not recognized file type.fileType={}",fileType);
            throw new RuntimeException("无法识别的文件类型");
        }
        return workbook;
    }

    public List<T> importExcel(String sheetName, String filePath,String fileType) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(filePath);
        return this.importExcel(sheetName,fileInputStream,fileType);
    }

    public List<T> importExcel(String sheetName, InputStream input,String fileType) throws Exception{
        List<T> list = new ArrayList<T>();
        try {
            Workbook workbook = getWorkbook(input,fileType);
            Sheet sheet = null;
            if (!sheetName.trim().equals("")) {
                sheet = workbook.getSheet(sheetName);// 如果指定sheet名,则取指定sheet中的内容.
            }
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);// 如果传入的sheet名不存在则默认指向第1个sheet.
            }
            int rows = sheet.getPhysicalNumberOfRows();// 得到数据的行数
            if (rows > 0) {// 有数据时才处理
                for (int i = 1; i < rows; i++) {// 从第2行开始取数据,默认第一行是表头.
                    Iterator<Cell> iterator = sheet.getRow(i).iterator();// 得到一行中的所有单元格对象.
                    T entity = null;

                    int j=0;
                    while(iterator.hasNext()){
                        Cell c = iterator.next();// 单元格中的内容.
                        c.setCellType(Cell.CELL_TYPE_STRING);  //设置为字符串

                        String content = c.getStringCellValue();
                        if (StringUtils.isBlank(content)){
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
                        // System.out.println(cells[j].getContents());
                        PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(j++);// 从map中得到对应列的field.
                        if(propertyDescriptor == null){
                            continue;
                        }
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = propertyDescriptor.getPropertyType();

                        Object val = null;
                        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            val = Integer.parseInt(content);
                        } else if (String.class == fieldType) {
                            val = String.valueOf(content);
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            val = Long.valueOf(content);
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            val = Float.valueOf(content);
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            val = Short.valueOf(content);
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            val = Double.valueOf(content);
                        } else if (Character.TYPE == fieldType || (Character.class == fieldType)) {
                            val = Character.valueOf(content.charAt(0));
                        }

                        propertyDescriptor.getWriteMethod().invoke(entity,val);
                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        }catch (Exception e) {
            log.error("catch a exception.",e);
            throw e;
        }
        return list;
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }
}
