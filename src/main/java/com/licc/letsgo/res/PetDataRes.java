package com.licc.letsgo.res;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/2 16:06
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = true)

public class PetDataRes extends BaseRes {
    PetsOnSaleRes data;
}

@Data
class PetsOnSale {
    private String id;
    private String petId;
    private int birthType;
    private int mutation;
    private int generation;
    private int rareDegree;
    private String desc;
    private int petType;
    private String amount;
    private String bgColor;
    private String petUrl;
    private String validCode;
}
@Data
class PetsOnSaleRes {
    private List<PetsOnSale> petsOnSale;
    private Integer          totalCount;
    private Boolean          hasData;
}