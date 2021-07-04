package org.wangfeng.panda.app.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class ExcelUtil <T> {


    /**
     * 导出Excel,可叠加
     * @param sheetName sheet名称
     * @param title 标题
     * @param list 内容
     * @param wb XSSFWorkbook对象
     * @return
     */
    public static XSSFWorkbook getXSSFWorkbook(String sheetName, List<String> title , List list, XSSFWorkbook wb,Class clazz){
        //1、先获取所有的导出的map
        Map<String , String> exportMap = new HashMap<>();
        if(clazz!=null){
            exportMap = ExcelUtil.getHeadersMap(clazz);
        }

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new XSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行
        XSSFRow row = sheet.createRow(0);

        //声明列对象
        XSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.size();i++){
            cell = row.createCell(i);
            cell.setCellValue(title.get(i));
        }

        //创建内容
        for(int i=0;i<list.size();i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<title.size();j++){
                //将内容按顺序赋给对应的列对象
                try {
                    //获得对应的方法
                    Object obj = list.get(i);
                    Object value = "";
                    if(clazz==null){
                        value = obj.toString();
                    }else{
                        Object newclass = obj.getClass().newInstance();
                        //获得对应的方法
                        Method m = newclass.getClass().getMethod("get" + StringUtils.toFirstCaseUpperCase(exportMap.get(title.get(j))));
                        value = m.invoke(list.get(i)).toString();
                    }
                    row.createCell(j).setCellValue(value==null?"":value.toString());
                } catch (Exception e) {
                }
            }
        }
        return wb;
    }












    /**
     * 读取出filePath中的所有数据信息
     * @param multipartFile
     * @param clazz
     * @param sheetNum
     */

    public static List getDataFromExcel(MultipartFile multipartFile, Class clazz, Integer sheetNum) {

        //1、先获取所有的导出的map
        Map<String , String> exportMap = ExcelUtil.getHeadersMap(clazz);

        List list = new ArrayList<>();

        //判断是否为excel类型文件
        if(!multipartFile.getOriginalFilename().endsWith(".xls")&&!multipartFile.getOriginalFilename().endsWith(".xlsx")) {
            System.out.println("文件不是excel类型");
        }

        FileInputStream fis =null;
        Workbook wookbook = null;

        try {
            //获取一个绝对地址的流
            fis = (FileInputStream)multipartFile.getInputStream();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        try {
            wookbook = new XSSFWorkbook(fis);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(sheetNum);

        //获得表头
        Row rowHead = sheet.getRow(0);

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();

        //获取所有的方法
        Method[] methods = clazz.getMethods();
        Map<String,Method> methodMap = new HashMap<>();
        for(Method method: methods){
            methodMap.put(method.getName(),method);
        }

        //获得所有数据
        for(int i = 1 ; i <= totalRowNum ; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            Object objClazz = null;
            try {
                objClazz = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (int j=0;j<rowHead.getLastCellNum();j++){
                try {
                    String variableNameCN = rowHead.getCell(j).getStringCellValue();
                    String variableName = exportMap.get(variableNameCN);
                    Method m = methodMap.get("set" + StringUtils.toFirstCaseUpperCase(variableName));
                    Cell cell = row.getCell(j);
                    cell.setCellType(CellType.STRING);
                    Object inputValue = typeTransform(cell.getStringCellValue(),m.getParameterTypes()[0]);
                    m.invoke(objClazz, inputValue);
                } catch (Exception e) {
                    System.out.println("error 了");
                }
            }
            list.add(objClazz);
        }

        return list;
    }




    /**
     * 列表页导出的header
     * @return
     */
    public static List<String> getHeaders(Class<?> type) {

        List<String> headers = new ArrayList<>();

        Field[] fields = type.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].isSynthetic()) {
                index ++;
                String colName = "";
                if (fields[i].getAnnotation(ExportColumn.class) != null) {
                    colName = fields[i].getAnnotation(ExportColumn.class).value();
                    headers.add(colName);
                }
            }
        }

        return headers;
    }

    /**
     * 得到所有的带ExportColumn 标签的字段名称和注释
     * @param type
     * @return
     */
    public static Map<String,String> getHeadersMap(Class<?> type){
        Map<String,String> resultMap = new HashMap<>();

        Field[] fields = type.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].isSynthetic()) {
                index ++;
                if (fields[i].getAnnotation(ExportColumn.class) != null) {
                    String colName = fields[i].getName();
                    String colNameCN = fields[i].getAnnotation(ExportColumn.class).value();
                    resultMap.put(colNameCN,colName);
                }
            }
        }
        return resultMap;
    }


    /**
     * 发送响应流
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 转换数据类型
     * @param value
     * @param clazz
     * @return
     */
    public static Object typeTransform(String value , Class clazz){

        if(value == null){
            return null;
        }

        Object returnObj = null;

        if (String.class.getName().equals(clazz.getName())) {
            returnObj = value;
        } else if (Integer.class.getName().equals(clazz.getName()) || int.class.getName().equals(clazz.getName())) {
            returnObj = Integer.valueOf(value);
        } else if (Long.class.getName().equals(clazz.getName()) || long.class.getName().equals(clazz.getName())) {
            returnObj = Long.parseLong(value);
        } else if (Double.class.getName().equals(clazz.getName()) || double.class.getName().equals(clazz.getName())) {
            returnObj = Double.parseDouble(value);
        } else if (Short.class.getName().equals(clazz.getName()) || short.class.getName().equals(clazz.getName())) {
            returnObj = Short.parseShort(value);
        } else if (Float.class.getName().equals(clazz.getName()) || float.class.getName().equals(clazz.getName())) {
            returnObj = Float.parseFloat(value);
        } else if (Date.class.getName().equals(clazz.getName())) {
            returnObj = DateUtils.getDate(value);
        } else if (BigDecimal.class.getName().equals(clazz.getName())) {
            returnObj = new BigDecimal(value);
        }

        return returnObj;
    }

}
