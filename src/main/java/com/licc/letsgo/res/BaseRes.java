package com.licc.letsgo.res;

import java.io.Serializable;
import lombok.Data;

/**
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/19 15:30
 * @see
 */
@Data
public class BaseRes  implements Serializable {
  private String errorNo;
  private String errorMsg;
  private String timestamp;
}
