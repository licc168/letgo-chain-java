package com.licc.letsgo.req;

import lombok.Data;

@Data
public class BuyReq extends  BaseRequest {
  private  String petId;
  private  String amount;
  private  String validCode;
  private  String captcha;
  private  String seed;
}
