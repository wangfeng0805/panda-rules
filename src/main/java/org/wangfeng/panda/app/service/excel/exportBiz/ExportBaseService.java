package org.wangfeng.panda.app.service.excel.exportBiz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.base.AppBaseService;

@Component
@Slf4j
public class ExportBaseService extends AppBaseService {

//    /**
//     * 全量导出，待废弃
//     * @throws Exception
//     */
//    public void exportAll() throws Exception {
//
//        //1、创建EXCEL模板
//        String fileName = "导出结果"+System.currentTimeMillis()+".xlsx";
//        XSSFWorkbook wb = new XSSFWorkbook();
//
//        List<String> insertSQL = new ArrayList<>();
//
//        //2、循环遍历所有的表
//        for(ExportTypeEnum exportTypeEnum: ExportTypeEnum.values()){
//
//            //2、查询所有的deleteflag为0的数据
//            String mapperFullPath = ExportTypeEnum.mapperPrePath+exportTypeEnum.getMapperName();
//            String classFullPath = ExportTypeEnum.classPrePath+exportTypeEnum.getClassName();
//
//            Class mapperClz = Class.forName(mapperFullPath);
//            MyMapper myMapper = (MyMapper) SpringUtil.getBean(mapperClz);
//
//            Class classClz = Class.forName(classFullPath);
//            Object classObj = classClz.newInstance();
//
//            Method m = mapperClz.getDeclaredMethod(ExportTypeEnum.queryAllMethod, classClz);
//
//            List result = (List) m.invoke(myMapper, classObj);
//
//            //3、得到sheet页面的header
//            String sheetName = exportTypeEnum.getCNName();
//            List<String> headers = ExcelUtil.getHeaders(classClz);
//
//            //4、生成excel
//            wb = ExcelUtil.getXSSFWorkbook(sheetName, headers, result, wb,classClz);
//
//
//            //5、生成导入语句
//            StringBuffer sb1 = new StringBuffer();
//            headers.stream().forEach(header->{
//                sb1.append(StringUtils.humpToUnderline(header)).append(",");
//            });
//            if(sb1.length()>0){
//                String preSQL = "INSERT INTO "+exportTypeEnum.getTableName()+"("+sb1.substring(0,sb1.length()-1)+")" ;
//
//                //创建内容
//                for(int i=0;i<result.size();i++){
//
//                    String valueSQL = "VALUES (";
//
//                    for(int j=0;j<headers.size();j++){
//                        //将内容按顺序赋给对应的列对象
//                        try {
//                            //获得对应的方法
//                            Object obj = result.get(i);
//                            Object newclass = obj.getClass().newInstance();
//                            //获得对应的方法
//                            Method method = newclass.getClass().getMethod("get" + StringUtils.toFirstCaseUpperCase(headers.get(j)));
//                            String value = method.invoke(result.get(i)).toString();
//
//                            if(value.contains("\\")){
//                                value = value.replace("\\","\\\\");
//                            }
//                            if(value.contains("'")){
//                                value = value.replace("'","\\'");
//                            }
//                            if(value.contains("\n")){
//                                value = value.replace("\n","");
//                            }
//
//                            if("true".equals(value)||"false".equals(value)){
//                                valueSQL = valueSQL+value+",";
//
//                            }else{
//                                valueSQL = valueSQL+"'"+value+"',";
//                            }
//
//                        } catch (Exception e) {
//                        }
//                    }
//
//                    valueSQL = valueSQL.substring(0,valueSQL.length()-1)+")";
//                    insertSQL.add(preSQL+valueSQL+";");
//                }
//            }
//        }
//
//
//        //6、把SQL语句导出到最后一个sheet页
//        String sqlSheetName = "需要导入的SQL";
//        List<String> sqlHeaders = new ArrayList<String>(){{
//            add("SQL");
//        }};
//        wb = ExcelUtil.getXSSFWorkbook(sqlSheetName, sqlHeaders, insertSQL, wb,null);
//
//        //6、导出到页面
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        try {
//            ExcelUtil.setResponseHeader(response, fileName);
//            OutputStream os = response.getOutputStream();
//            wb.write(os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            log.error("导出到页面出错");
//            e.printStackTrace();
//        }
//    }











}
