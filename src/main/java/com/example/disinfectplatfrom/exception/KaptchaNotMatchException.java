package com.example.disinfectplatfrom.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * @author : Lin
 * @version : [v1.0]
 * @className : KaptchaNotMatchException
 * @description : 自定义验证码认证异常
 * @createTime : 2023/3/2 19:29
 * @updateUser : Lin
 * @updateTime : 2023/3/2 19:29
 * @updateRemark : 描述说明本次修改内容
 */
public class KaptchaNotMatchException extends AuthenticationException {
    public KaptchaNotMatchException(String msg){super(msg);}
    public KaptchaNotMatchException(String msg,Throwable cause){super(msg,cause);}
}
