package com.licc.letsgo;

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
  public static final  String REDIS_PROXY_IP = "letgo_poxy_ip";

  /**莱茨狗可用代理IP池***/
  public static final  String REDIS_PROXY_LIST = "letgo_poxy_list";

  /**抓狗开关***/
  public static final  String REDIS_LETGO_ENABLE = "letgo_enable";


  public static final  String REDIS_BUY_LETGO_SUCCESS = "buy_letgo_success";
  public static final  String REDIS_BUY_LETGO_FAIL = "buy_letgo_fail";
  public static final  String REDIS_BUY_CACHE_ID = "buy_cache_id";
  /**验证码***/
  public static final  String REDIS_CAPTCHAS = "captchas";

}
