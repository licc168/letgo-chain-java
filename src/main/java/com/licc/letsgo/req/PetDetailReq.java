package com.licc.letsgo.req;

import lombok.Data;

/**
 *  莱茨狗详情入参
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/20 11:42
 * @see
 */
@Data
public class PetDetailReq extends  BaseRequest {
 private  String timeStamp;
 private  String petId;
 private  String nounce;
 private String token;
}
