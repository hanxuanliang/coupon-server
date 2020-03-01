package com.hxl.coupon.schedule;

import com.hxl.coupon.dao.CouponTemplateDao;
import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 定时清理无用的优惠券
 * @Author: hanxuanliang
 * @Date: 2020/3/1 21:27
 */
@Slf4j
@Component
public class ScheduledTask {

    private final CouponTemplateDao templateDao;

    @Autowired
    public ScheduledTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * <h2>下线已过期的优惠券模板,1分钟执行一次</h2>
     */
    @Scheduled(fixedDelay = 60*60*1000)
    @SuppressWarnings("all")
    public void offlineCouponTemplate(){
        log.info("开始执行 offlineCouponTemplate...");
        // 找到哪些未过期的优惠券
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if (CollectionUtils.isEmpty(templates)){
            log.info("offlineCouponTemplate 执行完成...");
            return;
        }

        List<CouponTemplate> expiredCouponTemplate = new ArrayList<>(templates.size());
        Date curDate = new Date();
        templates.stream().forEach(
                        t -> {
                            TemplateRule rule = t.getTemplateRule();
                            if (rule.getExpiration().getDeadline() < curDate.getTime()){
                                t.setExpired(true);
                                expiredCouponTemplate.add(t);
                            }
                        }
                );
        if (CollectionUtils.isNotEmpty(expiredCouponTemplate)){
            log.info("Expired CouponTemplate Num: {}", templateDao.saveAll(expiredCouponTemplate));
        }
        log.info("offlineCouponTemplate 执行完成...");

    }
}
