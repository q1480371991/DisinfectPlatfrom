package com.example.disinfectplatfrom.Config;

import com.example.disinfectplatfrom.Filter.LoginFilter;
import com.example.disinfectplatfrom.Service.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailService myUserDetailService;
    @Autowired
    public SecurityConfig(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
//        return inMemoryUserDetailsManager;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()//所有请求必须认证
                .and().formLogin()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler((res,resp,authentication)->{
                    Map<String,Object> result=new HashMap<String,Object>();
                    result.put("msg","注销成功");
                    result.put("用户信息",authentication.getPrincipal());
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setContentType("application/json;charset=UTF-8");
                    String s = new ObjectMapper().writeValueAsString(result);
                    resp.getWriter().println(s);
                })
                .and()
                .rememberMe()//开启记住我功能
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req,resp,ex)->{
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请认证之后再去处理！");
                })
                .and()
                .csrf().disable();

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//    @Bean
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
    //自定义filter交给工厂管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证 url
        loginFilter.setUsernameParameter("username");//指定接收json用户名key
        loginFilter.setPasswordParameter("password");//指定接收json密码key
        loginFilter.setAuthenticationManager(authenticationManagerBean());

        loginFilter.setAuthenticationSuccessHandler((req,resp,authentication)->{
            Map<String,Object> result=new HashMap<String,Object>();
            result.put("msg","登录成功");
            result.put("用户信息",authentication.getPrincipal());
            resp.setStatus(HttpStatus.OK.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });//认证成功处理
        loginFilter.setAuthenticationFailureHandler((req,resp,ex)->{
            Map<String,Object> result=new HashMap<String,Object>();
            result.put("msg","登录失败"+ex.getMessage());
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });//认证失败处理
        return loginFilter;
    }

    //行不通
//    @Bean
//    public com.example.disinfectplatfrom.Pojo.User CurrentUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        com.example.disinfectplatfrom.Pojo.User user = (com.example.disinfectplatfrom.Pojo.User)authentication.getPrincipal();
//        return user;
//    }
}
