package com.licc.letsgo.util;

import com.licc.letsgo.enums.EResultCode;

/**
 * @author lichangchao
 */
public class ResponseVo<T> {
    private static final long serialVersionUID = -3819569459544701549L;
    private Integer code;
    private String desc;
    private T data;

    private ResponseVo() {
    }

    public Integer getCode() {
        return this.code;
    }

    ResponseVo setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getDesc() {
        return this.desc;
    }

    ResponseVo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public ResponseVo setData(T data) {
        this.data = data;
        return this;
    }

    public Boolean isSuccess() {
        if (this.code == EResultCode.SUCCESS.getKey()) {
            return true;
        } else {
            return false;
        }
    }


    static ResponseVo BUILDER() {
        return new ResponseVo();
    }
}
