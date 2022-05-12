package com.jiangnx.community.entity;

public class Page {
    //当前页面，默认值为1
    private Integer current = 1;
    //页面大小，默认值为10
    private Integer limit = 10;
    //查询路径，当在前端页面点击时就会跳转到这个页面
    private  String path;
    //数据总数
    private int total;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if(current >= 1)
        this.current = current;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit >= 1 && limit <80)
        this.limit = limit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 获取页面总数total/limit
     * @return
     */
    public Integer getPageTotal(){
        if (total%limit == 0){
            return total/limit;
        }else {
            return total/limit + 1;
        }
    }

    public Integer getOffset(){
        return (current - 1) * limit;
    }

    public Integer from(){
        Integer from = current - 2;
        return from > 0?from:1;
    }

    public Integer to(){
        Integer to = current + 2;
        return to > getPageTotal()?getPageTotal():to;
    }

}
