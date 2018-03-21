package com.licc.letsgo.exception;


import com.licc.letsgo.util.ResponseVo;
import com.licc.letsgo.util.ResponseVoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionTranslator {
     final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseVo loginException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseVoUtil.failResult(e.getMessage());
    }




}
