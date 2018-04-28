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
public class LetgoBuyPetSaleThread implements Runnable {

    LetsgoService letsgoService;
    PetsOnSale sale;
    String ruleName;

    private User user;


    public LetgoBuyPetSaleThread(String ruleName,PetsOnSale sale, LetsgoService letsgoService, User user) {
        this.user = user;
        this.letsgoService = letsgoService;
        this.ruleName = ruleName;
        this.sale =sale;
    }

    @Override
    public void run() {
        letsgoService.buy(ruleName,sale,user);
    }
}
