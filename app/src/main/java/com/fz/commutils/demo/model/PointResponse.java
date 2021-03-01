package com.fz.commutils.demo.model;

import java.util.List;

/**
 * 积分列表数据返回对象
 *
 * @author yeshunda
 * @version 1.1
 * @date 2016/8/31
 * @since 1.0
 */
public class PointResponse {


    /**
     * page : 1
     * page_size : 20
     * total : 85
     * total_page : 5
     * data : []
     */

    private int page;
    private int page_size;
    private String total;
    /**
     * 积分总数
     */
    private String avaid_point;
    /**
     * 当前积分可兑换
     */
    private String rewards_tips;
    /**
     * 积分过期提示
     */
    private String expire_tips;
    private int total_page;

    private List<PointBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<PointBean> getData() {
        return data;
    }

    public void setData(List<PointBean> data) {
        this.data = data;
    }

    public String getAvaid_point() {
        return avaid_point;
    }

    public void setAvaid_point(String avaid_point) {
        this.avaid_point = avaid_point;
    }

    public String getRewards_tips() {
        return rewards_tips;
    }

    public void setRewards_tips(String rewards_tips) {
        this.rewards_tips = rewards_tips;
    }

    public String getExpire_tips() {
        return expire_tips;
    }

    public void setExpire_tips(String expire_tips) {
        this.expire_tips = expire_tips;
    }
}