package com.licc.letsgo.res.list;

import lombok.Data;

import java.util.List;

@Data
public class PetLets {
    Integer pageNo;
    Integer pageSize;
    Integer totalCount;
    List<PetLetsDataList> dataList;
}