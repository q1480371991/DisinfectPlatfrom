package com.example.disinfectplatfrom.Filter;

import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.exception.KaptchaNotMatchException;
import com.example.disinfectplatfrom.session.MySessionRegistryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义前后端分离认证 Filter
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    public static final String FORM_KAPTCHA_KEY="kaptcha";
    private String kaptchaParameter=FORM_KAPTCHA_KEY;
    @Autowired
    private MySessionRegistryImpl sessionRegistry;
    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("========================================");
        //1.判断是否是 post 方式请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断是否是 json 格式请求类型
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            //3.从 json 数据中获取用户输入用户名、密码及验证码进行认证 {"uername":"xxx","password":"xxx","kaptcha":"xxx","remeber-me":"xxx"}
            try {
                Map<String, Object> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                System.out.println(userInfo);
                String kaptchaParameter = getKaptchaParameter();
                String usernameParameter = getUsernameParameter();
                String passwordParameter = getPasswordParameter();
                String kaptcha= (String) userInfo.get(kaptchaParameter);
                String username = (String)userInfo.get(usernameParameter);
                String password = (String)userInfo.get(passwordParameter);
                boolean remembermeValue = (boolean)userInfo.get(AbstractRememberMeServices.DEFAULT_PARAMETER);

                if(!ObjectUtils.isEmpty(remembermeValue)){
                    if (remembermeValue){
                        request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER,"true");
                    }else {
                        request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER,"false");
                    }

                }
                System.out.println("用户名: " + username + " 密码: " + password+" 验证码："+kaptcha+" remember-me："+remembermeValue);
                //获取session中的验证码
                System.out.println("login sessionid:"+request.getSession().getId());
                String sessionVerifyCode =(String) request.getSession().getAttribute(getKaptchaParameter());
                if(!ObjectUtils.isEmpty(kaptcha)&&!ObjectUtils.isEmpty(sessionVerifyCode)&&kaptcha.equalsIgnoreCase(sessionVerifyCode))
                {
                    //验证码通过，认证
                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request, authRequest);
                    Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
                    sessionRegistry.registerNewSession(request.getSession().getId(),authenticate.getPrincipal());
                    return authenticate;
                }

            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
        throw new KaptchaNotMatchException("验证码不匹配！");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
