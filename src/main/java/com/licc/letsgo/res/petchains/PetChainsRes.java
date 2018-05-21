package com.licc.letsgo.res.petchains;

import com.licc.letsgo.res.BaseRes;
import java.util.List;
import lombok.Data;

/**
 * 信息链
 */
@Data
public class PetChainsRes extends BaseRes {
    PetLetData data;
}

@Data
class PetLetData {
    Integer         pageNo;
    Integer         pageSize;
    Integer         totalCount;
    List<PetLetsDataList > dataList;
}

@Data
class PetLetsDataList {
    Integer   type;
    Long      endTime;
    PetLetsgo father;
    PetLetsgo mother;
    String    ownerName;
    Long      blockchainSeq;
    String    txnHash;
    Integer   role;
    String    value;
    String    serviceFee;
    String    fee;
    String    partnerName;
    PetLetsgo partner;
    PetLetsgo child;
}

@Data
class PetLetsgo {
    String  id;
    String  petId;
    Integer rareDegree;
    Integer generation;
    String  petName;
    String  petUrl;
    String  bgColor;
    String  coolingInterval;

}
