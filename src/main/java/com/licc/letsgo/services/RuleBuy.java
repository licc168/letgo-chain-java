package com.licc.letsgo.services;

import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.res.petdetail.PetDetailAttr;
import com.licc.letsgo.res.petdetail.PetDetailData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购买规则-不采用规则引擎
 */
@Service
public class RuleBuy {
    final Logger logger = LoggerFactory.getLogger(LetsgoService.class);
    @Resource
    LetsgoService letsgoService;

    @Async
    public void buyRareDegree(PetsOnSale sale, User user) {
        int rareDegree = sale.getRareDegree();
        int generation = sale.getGeneration();
        boolean cooling = sale.getIsCooling();
        String coolingInterval = sale.getCoolingInterval();
        float amount = Float.parseFloat(sale.getAmount());
        boolean isBuy = false;
        if (generation == 0) {
            //处女狗
            if (!cooling && "0分钟".equals(coolingInterval)) {
                switch (rareDegree) {

                    case 3://史诗
                        isBuy = amount < 2000;
                        break;
                    case 4://神话
                        isBuy = amount < 400000;
                        break;
                    case 5://传说
                        isBuy = amount < 1000000;
                        break;

                }
            } else {
                switch (rareDegree) {
                    case 3://史诗
                        isBuy = amount < 1000;
                        break;
                    case 4://神话
                        isBuy = amount < 300000;
                        break;
                    case 5://传说
                        isBuy = amount < 1000000;
                        break;

                }

            }

        } else {
            switch (rareDegree) {
                case 3://史诗
                    isBuy = amount < 1000;
                    break;
                case 4://神话
                    isBuy = amount < 200000;
                    break;
                case 5://传说
                    isBuy = amount < 1000000;
                    break;

            }

        }
        if (isBuy) {
            letsgoService.buy("级别-规则", sale, user);
        }

    }

    @Async
    public void buyAttr(PetsOnSale petsale, PetDetailData detail, User user) {
        int rareDegree = petsale.getRareDegree();
        float amount = Float.parseFloat(detail.getAmount());
        String body = detail.getAttributes().get(0).getValue();
        String eye = detail.getAttributes().get(2).getValue();
        String mouth = detail.getAttributes().get(4).getValue();
        String dupiColor = detail.getAttributes().get(5).getValue();
        String bodyColor = detail.getAttributes().get(6).getValue();
        int generation = detail.getGeneration();
        boolean isCooling = petsale.getIsCooling();
        String coolingInterval = petsale.getCoolingInterval();
        int length = getXY(detail);
        boolean isBuy = false;
        if (generation == 0 && !isCooling && "0分钟".equals(coolingInterval)) {
            switch (rareDegree) {
                case 2:
                    if ("天使".equals(body) && "白眉斗眼".equals(eye)) {
                        logger.info("属性规则-【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                        isBuy = amount < 3000;
                        if (!isBuy && "甜蜜蜜".equals(mouth)) {
                            isBuy = amount < 3000;
                        }
                        if (!isBuy && "樱桃".equals(mouth)) {
                            isBuy = amount < 70000;
                        }
                    }
                    if (!isBuy && "天使".equals(body) && "小对眼".equals(eye) && "樱桃".equals(mouth)) {
                        isBuy = amount < 8000;
                    }
                    break;
                case 3:
                    switch (length) {
                        case 4:
                            if ("天使".equals(body) && "白眉斗眼".equals(eye)) {
                                logger.info("属性规则-4稀【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                                isBuy = amount < 40000;
                                if (!isBuy && "甜蜜蜜".equals(mouth)) {
                                    isBuy = amount < 40000;
                                }
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 200000;
                                }
                            }
                            if (!isBuy && "天使".equals(body) && "小对眼".equals(eye)) {
                                isBuy = amount < 7000;
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 20500;
                                }
                            }
                            if (!isBuy && "角鲸".equals(body) && "白眉".equals(eye)) {
                                isBuy = amount < 8000;
                            }
                            break;
                        case 5:
                            isBuy = amount < 19000;
                            if (!isBuy && "天使".equals(body) && "白眉斗眼".equals(eye)) {
                                logger.info("属性规则-5稀【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                                isBuy = amount < 80000;
                                if (!isBuy && "甜蜜蜜".equals(mouth)) {
                                    isBuy = amount < 90000;
                                }
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 600000;
                                }
                            }
                            if (!isBuy && "天使".equals(body) && "小对眼".equals(eye)) {
                                isBuy = amount < 28000;
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 80500;
                                }
                            }
                    }
                    break;
                case 4:
                    if ("天使".equals(body) && "小对眼".equals(eye)) {
                        isBuy = amount < 508000;
                        if (!isBuy && "樱桃".equals(mouth)) {
                            isBuy = amount < 680500;
                        }
                    }
                    if (!isBuy && "天使".equals(body) && "白眉斗眼".equals(eye)) {
                        isBuy = amount < 808000;
                    }
                    if (length == 7) {
                        isBuy = amount < 1001000;
                    }

            }
        } else {
            switch (rareDegree) {
                case 2:
                    if ("天使".equals(body) && "白眉斗眼".equals(eye)) {
                        logger.info("属性规则-【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                        isBuy = amount < 3000;
                        if (!isBuy && "甜蜜蜜".equals(mouth)) {
                            isBuy = amount < 3000;
                        }
                        if (!isBuy && "樱桃".equals(mouth)) {
                            isBuy = amount < 30000;
                        }
                    }
                    if (!isBuy && "天使".equals(body) && "小对眼".equals(eye) && "樱桃".equals(mouth)) {
                        isBuy = amount < 1000;
                    }
                    break;
                case 3:
                    switch (length) {
                        case 4:
                            if ("天使".equals(body) && "白眉斗眼".equals(eye)) {
                                logger.info("属性规则-4稀【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                                isBuy = amount < 18000;
                                if (!isBuy && "甜蜜蜜".equals(mouth)) {
                                    isBuy = amount < 18000;
                                }
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 100000;
                                }
                            }
                            if (!isBuy && "天使".equals(body) && "小对眼".equals(eye)) {
                                isBuy = amount < 2500;
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 5000;
                                }
                            }
                            if (!isBuy && "天使".equals(body) && "樱桃".equals(eye)) {
                                isBuy = amount < 5000;
                            }
                            break;
                        case 5:
                            isBuy = amount < 5000;
                            if (!isBuy && "天使".equals(body) && "白眉斗眼".equals(eye)) {
                                logger.info("属性规则-5稀【id:" + detail.getId() + "】" + "【" + amount + "  " + eye + "  " + mouth + "】" + "【级别：" + rareDegree + "】");
                                isBuy = amount < 60000;
                                if (!isBuy && "甜蜜蜜".equals(mouth)) {
                                    isBuy = amount < 60000;
                                }
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 300000;
                                }
                            }
                            if (!isBuy && "天使".equals(body) && "小对眼".equals(eye)) {
                                isBuy = amount < 11000;
                                if (!isBuy && "樱桃".equals(mouth)) {
                                    isBuy = amount < 20500;
                                }
                            }
                    }
                    break;
                case 4:
                    if ("天使".equals(body) && "小对眼".equals(eye)) {
                        isBuy = amount < 208000;
                        if (!isBuy && "樱桃".equals(mouth)) {
                            isBuy = amount < 200500;
                        }
                    }
                    if (!isBuy && "天使".equals(body) && "白眉斗眼".equals(eye)) {
                        isBuy = amount < 408000;
                    }
                    if (length == 7) {
                        isBuy = amount < 701000;
                    }

            }


        }
        if (isBuy) {
            letsgoService.buy("属性-规则", petsale, user);
        }
    }

    int getXY(PetDetailData detail) {
        int count = 0;
        List<PetDetailAttr> list = detail.getAttributes();
        for (PetDetailAttr attr : list) {
            String rareDegree = attr.getRareDegree();
            if ("稀有".equals(rareDegree)) {
                count = count + 1;
            }
        }
        return count;
    }

    @Async
    public void buyBirth(PetsOnSale sale, User user) {
        String id = sale.getId();
        float amount = Float.parseFloat(sale.getAmount());
        int rareDegree = sale.getRareDegree();
        boolean isBuy = false;
        if (id.matches("(19(7|8|9)|200)\\d{1}(0[1-9]|1[0-2])(0[1-9]|((1|2)[0-9])|3[0-1])") &&
                !id.matches("\\d{4}02(29|30|31)")) {
            logger.info("生日规则：【id:" + id + "】【金额:" + amount + "】【级别:" + rareDegree + "】");
            switch (rareDegree) {
                case 0:
                    isBuy = amount < 20000;
                    break;
                case 1:
                    isBuy = amount < 20000;
                    break;
                case 2:
                    isBuy = amount < 20000;
                    break;
                case 3:
                    isBuy = amount < 40000;
                    break;
                case 4:
                    isBuy = amount < 200000;
                    break;
                case 5:
                    isBuy = amount < 2000000;
                    break;
            }
        }
        if (isBuy) {
            letsgoService.buy("属性-规则", sale, user);
        }
    }
}
