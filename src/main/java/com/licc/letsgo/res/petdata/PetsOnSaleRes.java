package com.licc.letsgo.res.petdata;

import java.util.List;

import lombok.Data;

@Data
public class PetsOnSaleRes {
     List<PetsOnSale> petsOnSale;
     Integer          totalCount;
     Boolean          hasData;
}