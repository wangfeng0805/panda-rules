package org.wangfeng.panda.app.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;


public class Paginate implements Serializable {


    public final static int PAGE_NUMBER = 20;
    private List pageList;
    /**
     * 当前页号
     */
    private int pageNo;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总记录数
     */
    private int totalCount = 0;
    /**
     * 每页记录数
     */
    private int pageSize = PAGE_NUMBER;
    /**
     * 返回下一页页数
     */
    private int nextPage;
    /**
     * 返回上一页页数
     */
    private int prePage;

    public int getBeginNum() {
        return (pageNo - 1) * pageSize + 1;
    }

    public int getEndNum() {
        return pageNo * pageSize + 1;
    }

    public Paginate(int pageNo, int pageSize) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public List getPageList() {
        return pageList;
    }

    public void setPageList(List pageList) {
        this.pageList = pageList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        if (getTotalCount() == 0) {
            setTotalPage(1);
        } else {
            if (getTotalCount() % getPageSize() == 0) {
                setTotalPage(getTotalCount() / getPageSize());
            } else {
                setTotalPage(getTotalCount() / getPageSize() + 1);
            }
        }
        return totalPage;
    }

    private void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public int getNextPage() {
        if (isLastPage()) {
            return getTotalPage();
        } else {
            return getPageNo() + 1;
        }
    }

    public int getPrePage() {
        if (isFirstPage()) {
            return 1;
        } else {
            return getPageNo() - 1;
        }
    }

    public boolean isLastPage() {
        if (getTotalPage() <= 0) {
            return true;
        } else {
            return getPageNo() >= getTotalPage();
        }
    }

    public boolean isFirstPage() {
        return getPageNo() <= 1;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
