package com.licc.letsgo;

import com.licc.letsgo.res.petdata.PetsOnSale;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 项目常量定义
 * @version 1.0.0
 * @date 2018/3/20 9:34
 * @see
 */
public class Const {
  /**代理池-可用代理***/
  public static final  String REDIS_USEFUL_PROXY = "useful_proxy";

  /**莱茨狗单个可用代理IP***/
  public static final  String REDIS_PROXY_IP = "jspider:letgo_poxy_ip";

  /**莱茨狗可用代理IP池***/
  public static final  String REDIS_PROXY_LIST = "jspider:letgo_poxy_list";

  /**抓狗开关***/
  public static final  String REDIS_LETGO_ENABLE = "jspider:letgo_enable:";
  public static final  String REDIS_BUY_LETGO_SUCCESS = "jspider:buy_letgo_success";
  public static final  String REDIS_BUY_LETGO_FAIL = "jspider:buy_letgo_fail";
  /**验证码***/
  public static final  String REDIS_CAPTCHAS = "jspider:captchas";
  public static Map<String, Boolean> cache = new ConcurrentHashMap<>(10000);
  public static ConcurrentLinkedQueue<String> proxyQueue = new ConcurrentLinkedQueue<String>();
  public static ConcurrentLinkedQueue<String> proxyQueueBak = new ConcurrentLinkedQueue<String>();
  public static ConcurrentLinkedQueue<String> proxyQueueBakBak = new ConcurrentLinkedQueue<String>();

  public static Map<String, String> proxyCache = new ConcurrentHashMap<String,String>();

  public static LinkedBlockingDeque<PetsOnSale> saleQueue  = new LinkedBlockingDeque<>(1000);

  public static boolean proxyFlag = true;


}
