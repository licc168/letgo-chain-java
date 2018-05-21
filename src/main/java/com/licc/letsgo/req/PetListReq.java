package com.licc.letsgo.req;

import lombok.Data;

@Data
public class PetListReq extends BaseRequest {
    private int    pageNo =1;
    private int    pageSize=10;
    private int    pageTotal =-1;
    private int    totalCount=0;
    private String timeStamp;
    private String nounce;
    private String token;

}
