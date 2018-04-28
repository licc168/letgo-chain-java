package com.licc.letsgo.execute;

import com.licc.letsgo.Const;
import com.licc.letsgo.model.Rule;
import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.res.petdetail.PetDetailRes;
import com.licc.letsgo.rule.BuyRule;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.services.RuleBuy;
import com.licc.letsgo.util.DateUtil;
import com.licc.letsgo.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程处理狗数据
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/28 19:09
 * @see
 */
public class LetgoBuyLastExecuteThread implements Runnable {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    LetsgoService letsgoService;


    RuleBuy ruleBuy;

    private User user;

    private int threadName;

    public LetgoBuyLastExecuteThread(RuleBuy ruleBuy, LetsgoService letsgoService, User user, int threadName) {
        this.user = user;
        this.letsgoService = letsgoService;

        this.ruleBuy = ruleBuy;
    }

    @Override
    public void run() {
        try {
            while (true) {
                PetsOnSale sale = Const.saleQueue.takeLast();
                Rule rule = new Rule();
                rule.setPetsOnSale(sale);
                String petId = sale.getPetId();
                logger.debug("【线程：" + threadName + "开始进入规则购买狗【" + sale.getId() + "】" + DateUtil.getNowDate());
                BuyRule.executeRuleRareDegree(rule, user, letsgoService);
                BuyRule.executeRuleLiangHao(rule, user, letsgoService);
                BuyRule.executeRuleBirth(rule, user, letsgoService);

                String proxyIp = letsgoService.getProxyIp();
                ResponseVo<PetDetailRes> detail = letsgoService.queryPetDeatil(proxyIp, petId);
                if (detail.isSuccess()) {
                    rule.setPetDetailData(detail.getData().getData());
                    BuyRule.executeRuleAttr(rule, user, letsgoService);
                }
//                ruleBuy.buyRareDegree(sale, user);
//                ruleBuy.buyBirth(sale, user);
//                String proxyIp = letsgoService.getProxyIp();
//                String petId = sale.getPetId();
//                ResponseVo<PetDetailRes> detail = letsgoService.queryPetDeatil(proxyIp, petId);
//                if (detail.isSuccess()) {
//                    ruleBuy.buyAttr(sale, detail.getData().getData(), user);
//                }
            }
        } catch (InterruptedException e) {
            logger.error("【用户：" + user.getName() + "】购买狗数据失败" + e.getMessage());
        }
    }
}
