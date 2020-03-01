package com.hxl.coupon.vo;

import com.hxl.coupon.enums.CouponCategory;
import com.hxl.coupon.enums.DistributeTarget;
import com.hxl.coupon.enums.PlatformLineCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 优惠券模板创建请求对象
 * @Author: hanxuanliang
 * @Date: 2019/12/17 18:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    private String name;
    private String logo;
    private String description;
    private String category;
    private Integer platformLine;
    private Integer count;
    private Long userId;
    private Integer target;
    private TemplateRule rule;

    public boolean validate() {
        boolean stringValid = StringUtils.isNotEmpty(name) &&
                StringUtils.isNotEmpty(logo) &&
                StringUtils.isNotEmpty(description);
        boolean enumValid = null != CouponCategory.of(category) &&
                null != PlatformLineCategory.of(platformLine) &&
                null != DistributeTarget.of(target);
        boolean numberValid = count > 0 && userId > 0;

        return stringValid && enumValid && numberValid && rule.validate();
    }
}
