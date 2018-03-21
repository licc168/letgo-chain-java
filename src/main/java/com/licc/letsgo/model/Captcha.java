package com.licc.letsgo.model;

import lombok.Data;

@Data
public class Captcha {
  private String seed;
  private String code;
  private String createtime;
  public Captcha(String seed,String code){
    this.seed = seed;
    this.code = code;
  }
}
