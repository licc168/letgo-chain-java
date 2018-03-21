package com.licc.letsgo.res.petdata;

import lombok.Data;

@Data
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
}
