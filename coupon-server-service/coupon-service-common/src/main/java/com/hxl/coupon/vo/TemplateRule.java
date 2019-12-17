package com.hxl.coupon.vo;

import com.hxl.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 优惠券领取规则对象类，保存类型为json
 * @Author: hanxuanliang
 * @Date: 2019/12/15 16:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {

    private Expiration expiration;
    private Discount discount;

    /** 每个人最多可以领取几张优惠券 */
    private Integer limitation;

    private Usage usage;
    /** 权重（可以和哪些优惠券叠加使用，同一类的优惠券一定不可能叠加使用）：
     * list[] ，优惠券的唯一编码
     */
    private String weight;

    public boolean validate() {
        return expiration.validate() && discount.validate()
                && limitation > 0 && usage.validate()
                && StringUtils.isNotEmpty(weight);
    }

    /**
     * @Description: 有效期限规则
     * @date: 2019/12/17 14:16
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration {
        // 对应PeriodType的code字段
        private Integer period;
        // 有效间隔，只对变动性有效期有效
        private Integer gap;
        // 优惠券的失效日期，两类规则都有效
        private Long deadline;

        boolean validate() {
            return null != PeriodType.of(period) && gap > 0 && deadline > 0;
        }
    }

    /**
     * @Description: 折扣，需要与类型配合相决定
     * @date: 2019/12/17 14:19
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Discount {
        // 额度：满减（20），折扣（85%），立减（10）
        private Integer quota;
        // 基准，需要慢多少才可以使用
        private Integer base;

        boolean validate() {
            return quota > 0 && base > 0;
        }
    }

    /**
     * @Description: 地址选择的时候使用范围
     * @date: 2019/12/17 14:23
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        // 地点：省份
        private String province;
        // 地点：城市
        private String city;
        // 商品类型：list[文娱，生鲜，家居，全品类]
        private String goodsType;

        boolean validate() {
            return StringUtils.isNotEmpty(province) &&
                    StringUtils.isNotEmpty(city) &&
                    StringUtils.isNotEmpty(goodsType);
        }
    }
}
