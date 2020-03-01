package com.hxl.coupon.service.impl;

import com.hxl.coupon.dao.CouponTemplateDao;
import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.service.ITemplateBaseService;
import com.hxl.coupon.vo.CouponTemplatesdk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 优惠券模板基础服务
 * @Author: hanxuanliang
 * @Date: 2019/12/18 15:19
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()) {
            throw new CouponException("Template is not Exist: " + id);
        }
        return template.get();
    }

    @Override
    public List<CouponTemplatesdk> findAllUsableTemplate() {
        List<CouponTemplate> templates =
                templateDao.findAllByAvailableAndExpired(true, false);
        return templates.stream().map(this::template2Templatesdk).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplatesdk> findIdsToTemplatesdk(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream()
                .map(this::template2Templatesdk)
                .collect(Collectors.toMap(CouponTemplatesdk::getId, Function.identity()));
    }

    private CouponTemplatesdk template2Templatesdk(CouponTemplate couponTemplate) {
        CouponTemplatesdk templatesdk = new CouponTemplatesdk();
        BeanUtils.copyProperties(couponTemplate, templatesdk);
        return templatesdk;
    }
}
