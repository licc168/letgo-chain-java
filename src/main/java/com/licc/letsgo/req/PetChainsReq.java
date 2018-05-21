package com.licc.letsgo.req;

import lombok.Data;

@Data
public class PetChainsReq extends BaseRequest

{
    private int    pageNo =1;
    private int    pageSize=10;
    private String timeStamp;
    private String petId;
    private String nounce;
    private String token;
}
