package org.wangfeng.panda.app.common.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;


public abstract class AppBaseService {

    private static final Log logger = LogFactory.getLog(AppBaseService.class);

    /**
     * 创建分页
     *
     * @param pageIndex
     * @param pageSize
     * @param dataList
     * @return
     */
    protected Paginate createPaginate(int pageIndex, int pageSize, List dataList) {
        Paginate paginate = new Paginate(pageIndex, pageSize);

        if (dataList.isEmpty()) {
            paginate.setPageList(Collections.emptyList());
        } else {
            paginate.setPageList(dataList);
        }

        if (dataList instanceof Page) {
            Page paginator = ((Page) dataList);
            if (paginator != null) {
                paginate.setTotalCount((int) paginator.getTotal());
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totalCount:" + paginate.getTotalCount() + "\ttotalPages:"
                    + paginate.getTotalPage() + "\tpageIndex:" + pageIndex
                    + "\tpageSize:" + pageSize);
        }
        return paginate;
    }

    /**
     * 对批量操作的集合数据进行分页
     *
     * @param batchList 批量操作的对象集合
     * @param pageSize  每页数据条数，根据对象的字段数量确定,计算规则:2100/字段数量
     * @return 返回一个List, 里面是分页后的List集合
     */
    protected List<List> pagingBatchOperationList(List batchList, int pageSize) {
        List<List> returnList = new ArrayList<List>();
        if (batchList.isEmpty()) {
            return returnList;
        }
        int beginIndex = 0;
        int allSize = batchList.size();
        int step = allSize % pageSize == 0 ? allSize / pageSize : allSize / pageSize + 1;
        int toIndex = 0;
        for (int i = 0; i < step; i++) {
            toIndex = (i + 1) * pageSize;
            if (toIndex > allSize) {
                toIndex = allSize;
            }
            beginIndex = i * pageSize;
            returnList.add(batchList.subList(beginIndex, toIndex));
        }
        return returnList;
    }

    protected void setPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
    }

    protected void setPage(int pageNo, int pageSize, String orderBy) {
        PageHelper.startPage(pageNo, pageSize, orderBy);
    }


    /**
     * 初始化新增时的必要字段
     *
     * @param obj
     */
    protected void initSaveWord(Object obj) {
        if (obj instanceof AppBaseModel) {
            ((AppBaseModel) obj).setCreatedTime(new Date());
            ((AppBaseModel) obj).setCreatedBy(Constants.SYS);
            ((AppBaseModel) obj).setModifiedTime(new Date());
            ((AppBaseModel) obj).setModifiedBy(Constants.SYS);
            ((AppBaseModel) obj).setDeleteFlag(Constants.SHORT_ZERO);
        }
        fillObject(obj);
    }


    /**
     * 初始化更新时的必要字段
     *
     * @param obj
     */
    protected void initUpdateWord(Object obj) {
        if (obj instanceof AppBaseModel) {
            ((AppBaseModel) obj).setModifiedTime(new Date());
            ((AppBaseModel) obj).setModifiedBy(Constants.SYS);
        }
    }


    /**
     * 补充为空的字段
     * @param object
     */
    protected void fillObject(Object object) {
        try {
            if (object != null) {
                Class<?> clz = object.getClass();
                // 获取实体类的所有属性，返回Field数组
                List<Field> fieldList = new ArrayList<>() ;
                while (clz != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                    fieldList.addAll(Arrays.asList(clz .getDeclaredFields()));
                    clz = clz.getSuperclass(); //得到父类,然后赋给自己
                }
                clz = object.getClass();
                for (Field field : fieldList) {
                    Class<?> type = field.getType();
                    String fieldName = field.getName();
                    fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method mGet = null;
                    Method mSet = null;

                    if (String.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, String.class);
                            mSet.invoke(object, "");
                        }
                    } else if (Integer.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Integer.class);
                            mSet.invoke(object, 0);
                        }
                    } else if (int.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, int.class);
                            mSet.invoke(object, 0);
                        }
                    } else if (Long.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Long.class);
                            mSet.invoke(object, 0L);
                        }
                    } else if (long.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, long.class);
                            mSet.invoke(object, 0L);
                        }
                    } else if (Double.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Double.class);
                            mSet.invoke(object, 0.00);
                        }
                    } else if (double.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, double.class);
                            mSet.invoke(object, 0.00);
                        }
                    } else if (Short.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Short.class);
                            mSet.invoke(object, (short) 0);
                        }
                    } else if (short.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, short.class);
                            mSet.invoke(object, (short) 0);
                        }
                    } else if (Float.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Float.class);
                            mSet.invoke(object, 0.00F);
                        }
                    } else if (float.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, float.class);
                            mSet.invoke(object, 0.00f);
                        }
                    } else if (Date.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Date.class);
                            mSet.invoke(object, new Date());
                        }
                    } else if (BigDecimal.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, BigDecimal.class);
                            mSet.invoke(object, new BigDecimal("0.00"));
                        }
                    }else if (Boolean.class.getName().equals(type.getName())){
                        mGet = (Method) clz.getMethod("get" + fieldName);
                        if (mGet.invoke(object) == null) {
                            mSet = (Method) clz.getMethod("set" + fieldName, Boolean.class);
                            mSet.invoke(object, false);
                        }
                    }

                }
            }
        } catch (Exception e) {
            return;
        }
    }


    /**
     * 识别%
     * @param object
     */
    protected void distinguishString(Object object) {

        try {
            if (object != null) {
                Class<?> clz = object.getClass();
                // 获取实体类的所有属性，返回Field数组
                Field[] fields = clz.getDeclaredFields();
                for (Field field : fields) {
                    Class<?> type = field.getType();
                    String fieldName = field.getName();
                    fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method mGet = null;
                    Method mSet = null;

                    if (String.class.getName().equals(type.getName())) {
                        mGet = (Method) clz.getMethod("get" + fieldName);

                        String result = null;
                        mSet = (Method) clz.getMethod("set" + fieldName, String.class);
                        if(mGet.invoke(object)==null){
                            mSet.invoke(object, null);
                        }else{
                            result = mGet.invoke(object).toString();
                            mSet.invoke(object, result.replace("%","\\%"));
                            mSet.invoke(object, result.replace("_","\\_"));

                        }
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }


}
