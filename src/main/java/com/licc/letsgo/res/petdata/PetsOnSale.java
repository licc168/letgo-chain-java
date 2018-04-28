package com.licc.letsgo.res.petdata;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"bgColor","desc","mutation","petUrl","validCode"})
public class PetsOnSale {
    String id;
    String petId;
    int    birthType;
    int    mutation;
    int    generation;
    int    rareDegree;
    String desc;
    int    petType;
    String amount;
    String bgColor;
    String petUrl;
    String validCode;
    Boolean isCooling;
    String coolingInterval;
    String incubateTime;
    String createtime;

}
