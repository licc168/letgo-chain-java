package com.licc.letsgo.req;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/6
 */
@Data
class BaseRequest implements Serializable {
    private Long    requestId;
    private Integer appId;
    private String  tpl;
}
