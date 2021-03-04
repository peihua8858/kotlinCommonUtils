package com.fz.commutils.demo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 产品信息实体
 * 标签规则：折扣值 大于50%折扣显示，否则不显示 右上角的标签优先级：活动氛围标>折扣标>自营销标
 * 下面的标签sale，clearance，app only价格取1个，COD 满足条件会显示，也就是最多2个
 *
 * @author Shyky
 * @version 1.2
 * @date 2016/8/11
 * @since 1.0
 */
public class ProductBean  implements Parcelable, MultiItemEntity{
    private String avg_rate;
    private String avg_rate_img;
    private String cat_id;
    private String cat_name;
    private String goods_name;
    private String cat_url;
    private String goods_attr;
    private String goods_full_title;
    /**
     * 商品大图片
     */
    private String goods_grid;
    private String goods_id;
    /**
     * 商品图片
     */
    private String goods_img;
    private int goods_number;
    private String goods_sn;
    /**
     * 后端返回的spu
     */
    private String product_sn;
    /**
     * 商品缩略图
     */
    private String goods_thumb;
    private String goods_title;
    private String goods_weight;
    /**
     * 是否24小时发货
     */
    private String is_24h_ship;
    private String is_best;
    /**
     * 是否捆绑
     */
    private boolean is_bundles;
    private String is_free_shipping;
    private String is_hot;
    /**
     * 0:不显示cod标识，1:显示
     **/
    private String is_cod;
    /**
     * 原生专题商品标题
     */
    private String plate_title;
    /**
     * 原生专题特殊布局，增加奇偶数来区分
     */
    private String flag = "";

    private String is_new;
    /**
     * 是否促销
     */
    private int is_promote;
    /**
     * 价格类型
     */
    private int price_type;
    /**
     * 购买数量
     */
    private int buy_number;
    private String market_price;
    private String shop_price;
    private int more_color;
    private String point_rate;
    private String promote_price;

    /**
     * 折扣值 大于50%折扣显示，否则不显示
     */
    private int promote_zhekou;
    private int qinquan;
    private String review_count;
    private int saveperce;
    private String saveprice;
    private int shareandsave;
    private String type;
    private String up;
    private String url_title;
    private String is_mobile_price;
    private String activityType;
    /**
     * 手机图片
     */
    private String wp_image;
    private List<TagsBean> tags;
    /**
     * 活动氛围图片
     */
    private String activityIcon;
    /**
     * 是否收藏
     */
    private int is_collect;

    /**
     * 自营销产品 1热卖品，2潜力品，3新品
     */
    private int channel_type;
    /**
     * 商品支付总额
     */
    private String subtotal;
    private String goodsPng;

    private GoodCatInfoBean cat_level_column;
    private List<String> pictures;
    /**
     * 促销类型(清仓>APP专享>SALE(满减）)提示语
     */
    private String sale_type;
    /**
     * 0不是多色标   1是多色标
     */
    private int same_color;
    /**
     * 用于适配器类型
     */
    private int itemType = 0;
    /**
     * 列表颜色选择操作数据
     */
    private List<ProductBean> groupGoodsList;
    /**
     * 颜色图片
     */
    private String color_img;
    /**
     * 颜色值，十六进制
     */
    private String color_code;
    /**
     * 选中的索引(商品列表色块需要滚动显示)
     */
    private int selectedPosition;
    /**
     * “1”打版商品,“0”非打版商品
     */
    private String is_making;
    /**
     * sku氛围活动文案
     */
    private String atmos;

    /**
     * 事件起点单位毫秒
     */
    private long elapsedRealTime;
    /**
     * 商品列表秒杀剩余时间,单位秒
     */
    private long seckill_countdown;
    /**
     * 上下装折后价格
     */
    private String discount_price;
    /**
     * 上下装折扣价格
     */
    private double youhui_price;

    public ProductBean() {
        /**
         * 记录接口返回数据时当前系统时间
         */
        elapsedRealTime = SystemClock.elapsedRealtime();
    }

    public long getCountDownEndTime() {
        return (elapsedRealTime + (seckill_countdown * 1000)) - SystemClock.elapsedRealtime();
    }

    public ProductBean(int itemType, String plate_title) {
        this.plate_title = plate_title;
        this.itemType = itemType;
    }

    public ProductBean(ProductBean other) {
        this.avg_rate = other.avg_rate;
        this.avg_rate_img = other.avg_rate_img;
        this.cat_id = other.cat_id;
        this.cat_name = other.cat_name;
        this.goods_name = other.goods_name;
        this.cat_url = other.cat_url;
        this.goods_attr = other.goods_attr;
        this.goods_full_title = other.goods_full_title;
        this.goods_grid = other.goods_grid;
        this.goods_id = other.goods_id;
        this.goods_img = other.goods_img;
        this.goods_number = other.goods_number;
        this.goods_sn = other.goods_sn;
        this.product_sn = other.product_sn;
        this.goods_thumb = other.goods_thumb;
        this.goods_title = other.goods_title;
        this.goods_weight = other.goods_weight;
        this.is_24h_ship = other.is_24h_ship;
        this.is_best = other.is_best;
        this.is_bundles = other.is_bundles;
        this.is_free_shipping = other.is_free_shipping;
        this.is_hot = other.is_hot;
        this.is_cod = other.is_cod;
        this.plate_title = other.plate_title;
        this.flag = other.flag;
        this.is_new = other.is_new;
        this.is_promote = other.is_promote;
        this.price_type = other.price_type;
        this.buy_number = other.buy_number;
        this.market_price = other.market_price;
        this.shop_price = other.shop_price;
        this.more_color = other.more_color;
        this.point_rate = other.point_rate;
        this.promote_price = other.promote_price;
        this.promote_zhekou = other.promote_zhekou;
        this.qinquan = other.qinquan;
        this.review_count = other.review_count;
        this.saveperce = other.saveperce;
        this.saveprice = other.saveprice;
        this.shareandsave = other.shareandsave;
        this.type = other.type;
        this.up = other.up;
        this.url_title = other.url_title;
        this.is_mobile_price = other.is_mobile_price;
        this.activityType = other.activityType;
        this.wp_image = other.wp_image;
        this.tags = other.tags;
        this.activityIcon = other.activityIcon;
        this.is_collect = other.is_collect;
        this.channel_type = other.channel_type;
        this.subtotal = other.subtotal;
        this.goodsPng = other.goodsPng;
        this.cat_level_column = other.cat_level_column;
        this.pictures = other.pictures;
        this.sale_type = other.sale_type;
        this.same_color = other.same_color;
        this.itemType = other.itemType;
        this.groupGoodsList = other.groupGoodsList;
        this.color_img = other.color_img;
        this.color_code = other.color_code;
        this.selectedPosition = other.selectedPosition;
        this.is_making = other.is_making;
        this.atmos = other.atmos;
        this.elapsedRealTime = other.elapsedRealTime;
        this.seckill_countdown = other.seckill_countdown;
        this.discount_price = other.discount_price;
        this.youhui_price = other.youhui_price;
    }

    public ProductBean(int itemType) {
        this.itemType = itemType;
    }

    public int getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(int channel_type) {
        this.channel_type = channel_type;
    }

    public String getAvg_rate() {
        return avg_rate;
    }

    public void setAvg_rate(String avg_rate) {
        this.avg_rate = avg_rate;
    }

    public String getAvg_rate_img() {
        return avg_rate_img;
    }

    public void setAvg_rate_img(String avg_rate_img) {
        this.avg_rate_img = avg_rate_img;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getCat_url() {
        return cat_url;
    }

    public void setCat_url(String cat_url) {
        this.cat_url = cat_url;
    }

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getGoods_full_title() {
        return goods_full_title;
    }

    public void setGoods_full_title(String goods_full_title) {
        this.goods_full_title = goods_full_title;
    }

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
    }

    public String getGoods_grid() {
        return goods_grid;
    }

    public void setGoods_grid(String goods_grid) {
        this.goods_grid = goods_grid;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_weight() {
        return goods_weight;
    }

    public void setGoods_weight(String goods_weight) {
        this.goods_weight = goods_weight;
    }

    public String getIs_24h_ship() {
        return is_24h_ship;
    }

    public void setIs_24h_ship(String is_24h_ship) {
        this.is_24h_ship = is_24h_ship;
    }

    public String getIs_best() {
        return is_best;
    }

    public void setIs_best(String is_best) {
        this.is_best = is_best;
    }

    public boolean isIs_bundles() {
        return is_bundles;
    }

    public void setIs_bundles(boolean is_bundles) {
        this.is_bundles = is_bundles;
    }

    public String getIs_free_shipping() {
        return is_free_shipping;
    }

    public void setIs_free_shipping(String is_free_shipping) {
        this.is_free_shipping = is_free_shipping;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_cod() {
        return is_cod;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public String getPlate_title() {
        return plate_title;
    }

    public void setPlate_title(String plate_title) {
        this.plate_title = plate_title;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public int getIs_promote() {
        return is_promote;
    }

    public void setIs_promote(int is_promote) {
        this.is_promote = is_promote;
    }

    public int getBuy_number() {
        return buy_number;
    }

    public void setBuy_number(int buy_number) {
        this.buy_number = buy_number;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public int getMore_color() {
        return more_color;
    }

    public void setMore_color(int more_color) {
        this.more_color = more_color;
    }

    public String getPoint_rate() {
        return point_rate;
    }

    public void setPoint_rate(String point_rate) {
        this.point_rate = point_rate;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public int getPrice_type() {
        return price_type;
    }

    public void setPrice_type(int price_type) {
        this.price_type = price_type;
    }

    public int getPromote_zhekou() {
        return promote_zhekou;
    }

    public void setPromote_zhekou(int promote_zhekou) {
        this.promote_zhekou = promote_zhekou;
    }

    public int getQinquan() {
        return qinquan;
    }

    public void setQinquan(int qinquan) {
        this.qinquan = qinquan;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public int getSaveperce() {
        return saveperce;
    }

    public void setSaveperce(int saveperce) {
        this.saveperce = saveperce;
    }

    public String getSaveprice() {
        return saveprice;
    }

    public void setSaveprice(String saveprice) {
        this.saveprice = saveprice;
    }

    public int getShareandsave() {
        return shareandsave;
    }

    public void setShareandsave(int shareandsave) {
        this.shareandsave = shareandsave;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getUrl_title() {
        return url_title;
    }

    public void setUrl_title(String url_title) {
        this.url_title = url_title;
    }

    public String getIs_mobile_price() {
        return is_mobile_price;
    }

    public void setIs_mobile_price(String is_mobile_price) {
        this.is_mobile_price = is_mobile_price;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getWp_image() {
        if (TextUtils.isEmpty(wp_image)) {
            return goods_img;
        }
        return wp_image;
    }

    public void setWp_image(String wp_image) {
        this.wp_image = wp_image;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tagsBeanList) {
        this.tags = tagsBeanList;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public GoodCatInfoBean getCat_level_column() {
        return cat_level_column;
    }

    public void setCat_level_column(GoodCatInfoBean cat_level_column) {
        this.cat_level_column = cat_level_column;
    }

    public String getGoodsPng() {
        return goodsPng;
    }

    public void setGoodsPng(String goodsPng) {
        this.goodsPng = goodsPng;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getSale_type() {
        return sale_type;
    }

    public void setSale_type(String sale_type) {
        this.sale_type = sale_type;
    }

    public int getSame_color() {
        return same_color;
    }

    public boolean isSameColor() {
        return same_color == 1;
    }

    public void setSame_color(int same_color) {
        this.same_color = same_color;
    }

    public List<ProductBean> getGroupGoodsList() {
        return groupGoodsList;
    }

    public void setGroupGoodsList(List<ProductBean> groupGoodsList) {
        this.groupGoodsList = groupGoodsList;
    }

    public String getColor_img() {
        return color_img;
    }

    public void setColor_img(String color_img) {
        this.color_img = color_img;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public boolean isInvalid() {
        return goods_number <= 0;
    }

    public String getIs_making() {
        return is_making;
    }

    public void setIs_making(String is_making) {
        this.is_making = is_making;
    }

    public String getAtmos() {
        return atmos;
    }

    public void setAtmos(String atmos) {
        this.atmos = atmos;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public double getYouhui_price() {
        return youhui_price;
    }

    public void setYouhui_price(double youhui_price) {
        this.youhui_price = youhui_price;
    }

    public String getProduct_sn() {
        return product_sn;
    }

    public void setProduct_sn(String product_sn) {
        this.product_sn = product_sn;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "avg_rate='" + avg_rate + '\'' +
                ", avg_rate_img='" + avg_rate_img + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", cat_name='" + cat_name + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", cat_url='" + cat_url + '\'' +
                ", goods_attr='" + goods_attr + '\'' +
                ", goods_full_title='" + goods_full_title + '\'' +
                ", goods_grid='" + goods_grid + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_img='" + goods_img + '\'' +
                ", goods_number=" + goods_number +
                ", goods_sn='" + goods_sn + '\'' +
                ", product_sn='" + product_sn + '\'' +
                ", goods_thumb='" + goods_thumb + '\'' +
                ", goods_title='" + goods_title + '\'' +
                ", goods_weight='" + goods_weight + '\'' +
                ", is_24h_ship='" + is_24h_ship + '\'' +
                ", is_best='" + is_best + '\'' +
                ", is_bundles=" + is_bundles +
                ", is_free_shipping='" + is_free_shipping + '\'' +
                ", is_hot='" + is_hot + '\'' +
                ", is_cod='" + is_cod + '\'' +
                ", plate_title='" + plate_title + '\'' +
                ", flag='" + flag + '\'' +
                ", is_new='" + is_new + '\'' +
                ", is_promote=" + is_promote +
                ", price_type=" + price_type +
                ", buy_number=" + buy_number +
                ", market_price='" + market_price + '\'' +
                ", shop_price='" + shop_price + '\'' +
                ", more_color=" + more_color +
                ", point_rate='" + point_rate + '\'' +
                ", promote_price='" + promote_price + '\'' +
                ", promote_zhekou=" + promote_zhekou +
                ", qinquan=" + qinquan +
                ", review_count='" + review_count + '\'' +
                ", saveperce=" + saveperce +
                ", saveprice='" + saveprice + '\'' +
                ", shareandsave=" + shareandsave +
                ", type='" + type + '\'' +
                ", up='" + up + '\'' +
                ", url_title='" + url_title + '\'' +
                ", is_mobile_price='" + is_mobile_price + '\'' +
                ", activityType='" + activityType + '\'' +
                ", wp_image='" + wp_image + '\'' +
                ", tags=" + tags +
                ", activityIcon='" + activityIcon + '\'' +
                ", is_collect=" + is_collect +
                ", channel_type=" + channel_type +
                ", subtotal='" + subtotal + '\'' +
                ", goodsPng='" + goodsPng + '\'' +
                ", cat_level_column=" + cat_level_column +
                ", pictures=" + pictures +
                ", sale_type='" + sale_type + '\'' +
                ", same_color=" + same_color +
                ", itemType=" + itemType +
                ", groupGoodsList=" + groupGoodsList +
                ", color_img='" + color_img + '\'' +
                ", color_code='" + color_code + '\'' +
                ", selectedPosition=" + selectedPosition +
                ", is_making='" + is_making + '\'' +
                ", atmos='" + atmos + '\'' +
                ", elapsedRealTime=" + elapsedRealTime +
                ", seckill_countdown=" + seckill_countdown +
                ", discount_price='" + discount_price + '\'' +
                ", youhui_price=" + youhui_price +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avg_rate);
        dest.writeString(this.avg_rate_img);
        dest.writeString(this.cat_id);
        dest.writeString(this.cat_name);
        dest.writeString(this.goods_name);
        dest.writeString(this.cat_url);
        dest.writeString(this.goods_attr);
        dest.writeString(this.goods_full_title);
        dest.writeString(this.goods_grid);
        dest.writeString(this.goods_id);
        dest.writeString(this.goods_img);
        dest.writeInt(this.goods_number);
        dest.writeString(this.goods_sn);
        dest.writeString(this.product_sn);
        dest.writeString(this.goods_thumb);
        dest.writeString(this.goods_title);
        dest.writeString(this.goods_weight);
        dest.writeString(this.is_24h_ship);
        dest.writeString(this.is_best);
        dest.writeByte(this.is_bundles ? (byte) 1 : (byte) 0);
        dest.writeString(this.is_free_shipping);
        dest.writeString(this.is_hot);
        dest.writeString(this.is_cod);
        dest.writeString(this.plate_title);
        dest.writeString(this.flag);
        dest.writeString(this.is_new);
        dest.writeInt(this.is_promote);
        dest.writeInt(this.price_type);
        dest.writeInt(this.buy_number);
        dest.writeString(this.market_price);
        dest.writeString(this.shop_price);
        dest.writeInt(this.more_color);
        dest.writeString(this.point_rate);
        dest.writeString(this.promote_price);
        dest.writeInt(this.promote_zhekou);
        dest.writeInt(this.qinquan);
        dest.writeString(this.review_count);
        dest.writeInt(this.saveperce);
        dest.writeString(this.saveprice);
        dest.writeInt(this.shareandsave);
        dest.writeString(this.type);
        dest.writeString(this.up);
        dest.writeString(this.url_title);
        dest.writeString(this.is_mobile_price);
        dest.writeString(this.activityType);
        dest.writeString(this.wp_image);
        dest.writeTypedList(this.tags);
        dest.writeString(this.activityIcon);
        dest.writeInt(this.is_collect);
        dest.writeInt(this.channel_type);
        dest.writeString(this.subtotal);
        dest.writeString(this.goodsPng);
        dest.writeParcelable(this.cat_level_column, flags);
        dest.writeStringList(this.pictures);
        dest.writeString(this.sale_type);
        dest.writeInt(this.same_color);
        dest.writeInt(this.itemType);
        dest.writeTypedList(this.groupGoodsList);
        dest.writeString(this.color_img);
        dest.writeString(this.color_code);
        dest.writeInt(this.selectedPosition);
        dest.writeString(this.is_making);
        dest.writeString(this.atmos);
        dest.writeLong(this.elapsedRealTime);
        dest.writeLong(this.seckill_countdown);
        dest.writeString(this.discount_price);
        dest.writeDouble(this.youhui_price);
    }

    protected ProductBean(Parcel in) {
        this.avg_rate = in.readString();
        this.avg_rate_img = in.readString();
        this.cat_id = in.readString();
        this.cat_name = in.readString();
        this.goods_name = in.readString();
        this.cat_url = in.readString();
        this.goods_attr = in.readString();
        this.goods_full_title = in.readString();
        this.goods_grid = in.readString();
        this.goods_id = in.readString();
        this.goods_img = in.readString();
        this.goods_number = in.readInt();
        this.goods_sn = in.readString();
        this.product_sn = in.readString();
        this.goods_thumb = in.readString();
        this.goods_title = in.readString();
        this.goods_weight = in.readString();
        this.is_24h_ship = in.readString();
        this.is_best = in.readString();
        this.is_bundles = in.readByte() != 0;
        this.is_free_shipping = in.readString();
        this.is_hot = in.readString();
        this.is_cod = in.readString();
        this.plate_title = in.readString();
        this.flag = in.readString();
        this.is_new = in.readString();
        this.is_promote = in.readInt();
        this.price_type = in.readInt();
        this.buy_number = in.readInt();
        this.market_price = in.readString();
        this.shop_price = in.readString();
        this.more_color = in.readInt();
        this.point_rate = in.readString();
        this.promote_price = in.readString();
        this.promote_zhekou = in.readInt();
        this.qinquan = in.readInt();
        this.review_count = in.readString();
        this.saveperce = in.readInt();
        this.saveprice = in.readString();
        this.shareandsave = in.readInt();
        this.type = in.readString();
        this.up = in.readString();
        this.url_title = in.readString();
        this.is_mobile_price = in.readString();
        this.activityType = in.readString();
        this.wp_image = in.readString();
        this.tags = in.createTypedArrayList(TagsBean.CREATOR);
        this.activityIcon = in.readString();
        this.is_collect = in.readInt();
        this.channel_type = in.readInt();
        this.subtotal = in.readString();
        this.goodsPng = in.readString();
        this.cat_level_column = in.readParcelable(GoodCatInfoBean.class.getClassLoader());
        this.pictures = in.createStringArrayList();
        this.sale_type = in.readString();
        this.same_color = in.readInt();
        this.itemType = in.readInt();
        this.groupGoodsList = in.createTypedArrayList(ProductBean.CREATOR);
        this.color_img = in.readString();
        this.color_code = in.readString();
        this.selectedPosition = in.readInt();
        this.is_making = in.readString();
        this.atmos = in.readString();
        this.elapsedRealTime = in.readLong();
        this.seckill_countdown = in.readLong();
        this.discount_price = in.readString();
        this.youhui_price = in.readDouble();
    }

    public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
        @Override
        public ProductBean createFromParcel(Parcel source) {
            return new ProductBean(source);
        }

        @Override
        public ProductBean[] newArray(int size) {
            return new ProductBean[size];
        }
    };
}