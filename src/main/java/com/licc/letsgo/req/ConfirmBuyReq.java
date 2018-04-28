package com.licc.letsgo.req;

import lombok.Data;

@Data
public class ConfirmBuyReq extends  BaseRequest {
 Integer confirmType;
 String nounce;
 String s;
 String token;
 Long timeStamp;

}
