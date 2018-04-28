package com.licc.letsgo.req;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetDataRequest extends BaseRequest {
    private int pageNo;
    private int pageSize;
    private String querySortType;
    private List<String> petIds;
    private String lastAmount;
    private String lastRareDegree;
    private  String filterCondition;

}
