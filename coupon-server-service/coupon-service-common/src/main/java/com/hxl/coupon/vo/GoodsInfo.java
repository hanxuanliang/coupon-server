package com.hxl.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 商品info
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {

    private double price;

    private int count;

    private int type;

}
