package com.licc.letsgo.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/20 9:24
 * @see
 */
public class DateUtil {
  public static String  getNowDate(){
    LocalDateTime ldt = LocalDateTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
    String strDate = dtf.format(ldt);
    return  strDate;

  }
}
