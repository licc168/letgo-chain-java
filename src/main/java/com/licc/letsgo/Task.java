package com.licc.letsgo;

import com.licc.letsgo.execute.LetGoprodDataThread;
import com.licc.letsgo.execute.LetgoBuyExecuteThread;
import com.licc.letsgo.execute.LetgoBuyLastExecuteThread;
import com.licc.letsgo.req.PetDataRequest;
import com.licc.letsgo.services.RuleBuy;
import com.licc.letsgo.util.DateUtil;

import java.util.HashMap;
import java.util.List;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.licc.letsgo.enums.PetsSortingEnum;
import com.licc.letsgo.model.Rule;
import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetDataRes;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.RedisUtils;
import com.licc.letsgo.util.ResponseVo;

/**
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/19 18:27
 * @see
 */
@Service
@Async
public class Task {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    RedisUtils redisUtils;
    @Resource
    LetsgoService letsgoService;
    @Resource
    RuleBuy ruleBuy;

    public void data(Integer type) {
        ExecutorService serviceGet = Executors.newFixedThreadPool(10);
        PetDataRequest request = new PetDataRequest();
        request.setPageSize(10);
        request.setQuerySortType(PetsSortingEnum.CREATETIME_DESC.name());
        request.setPageNo(1);
        switch (type) {
            case 0://时间倒序

                break;
            case 1://ss
                request.setFilterCondition("{\"1\":\"3\",\"3\":\"0-1\",\"6\":\"1\"}");
                request.setQuerySortType(PetsSortingEnum.AMOUNT_ASC.name());
                break;
            case 2://ss
                request.setFilterCondition("{\"1\":\"4\",\"3\":\"0-1\",\"6\":\"1\"}");
                request.setQuerySortType(PetsSortingEnum.AMOUNT_ASC.name());
                break;
            case 3://ss
                request.setPageNo(2);
                break;
            case 4://ss
                request.setPageNo(3);
                break;
            case 5://ss
                request.setPageNo(4);
                break;
            case 6://ss
                request.setPageNo(5);
                break;

        }
        serviceGet.execute(new LetGoprodDataThread(letsgoService, redisUtils, request));
        serviceGet.shutdown();

//        request.setQuerySortType(PetsSortingEnum.AMOUNT_ASC.name());
//        request.setFilterCondition("{\"1\":\"4\",\"3\":\"0-1\",\"6\":\"1\"}");
//        serviceGet.execute(new LetGoprodDataThread(letsgoService, redisUtils, user, request));
//
//        request.setFilterCondition("{\"1\":\"3\",\"3\":\"0-1\",\"6\":\"1\"}");
//        serviceGet.execute(new LetGoprodDataThread(letsgoService, redisUtils, user, request));


    }


    public void buyFrist(User user) {
        ExecutorService serviceBuy = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 50; i++) {

            serviceBuy.execute(new LetgoBuyExecuteThread(ruleBuy, letsgoService, user, i));
        }

        serviceBuy.shutdown();
    }
    public void buyLast(User user) {
        ExecutorService serviceBuy = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 50; i++) {
            serviceBuy.execute(new LetgoBuyLastExecuteThread(ruleBuy, letsgoService, user, i));
        }

        serviceBuy.shutdown();
    }
}
//      request.setFilterCondition("{\"1\":\"4\",\"3\":\"0-1\",\"6\":\"1\"}");
