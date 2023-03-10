package com.example.disinfectplatfrom.Service.ServiceImpl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : MyPersistentTokenBasedRemeberMeServiceImpl
 * @description : 描述说明该类的功能
 * @createTime : 2023/3/3 17:57
 * @updateUser : Lin
 * @updateTime : 2023/3/3 17:57
 * @updateRemark : 描述说明本次修改内容
 */
public class MyPersistentTokenBasedRemeberMeServiceImpl extends PersistentTokenBasedRememberMeServices {

    public MyPersistentTokenBasedRemeberMeServiceImpl(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /*
     * @title :rememberMeRequested
     * @Author :Lin
     * @Description ：自定义前后端分离获取remeber-me方式
     * @Date :17:58 2023/3/3
     * @Param :[request, parameter]
     * @return :boolean
     **/
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        Object attribute = request.getAttribute(parameter);
        if(!ObjectUtils.isEmpty(attribute)){
            String paramValue = attribute.toString();
            if (!ObjectUtils.isEmpty(paramValue)) {
                if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                        || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                    return true;
                }
            }
        }

        return false;
    }
}
