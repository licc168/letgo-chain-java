package com.licc.letsgo.model;

import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.res.petdetail.PetDetailData;
import lombok.Data;

@Data
public class Rule {
  PetsOnSale petsOnSale;
  PetDetailData petDetailData;
}
