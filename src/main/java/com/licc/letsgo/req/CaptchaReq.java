package com.licc.letsgo.req;

import lombok.Data;

@Data
public class CaptchaReq extends  BaseRequest {
  String token;
  String nounce;
  String tpl;
}
