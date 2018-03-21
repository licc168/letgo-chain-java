package com.licc.letsgo.res.petdata;

import com.licc.letsgo.res.BaseRes;

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

