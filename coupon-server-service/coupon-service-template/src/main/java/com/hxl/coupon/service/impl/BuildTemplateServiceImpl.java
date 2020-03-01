package com.hxl.coupon.service.impl;

import com.hxl.coupon.dao.CouponTemplateDao;
import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.service.IAsyncService;
import com.hxl.coupon.service.IBuildTemplateService;
import com.hxl.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 构建优惠券模板接口实现
 * @Author: hanxuanliang
 * @Date: 2019/12/18 14:51
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    /** 异步服务 */
    private final IAsyncService asyncService;
    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;

    public BuildTemplateServiceImpl(IAsyncService asyncService, CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {
        if (!request.validate()) {
            throw new CouponException("");
        }
        if (null != templateDao.findByName(request.getName())) {
            throw new CouponException("Exist Same Name Template");
        }
        CouponTemplate template = new CouponTemplate();
        BeanUtils.copyProperties(request, template);
        asyncService.asyncContructCouponTemplate(template);
        return template;
    }
}
