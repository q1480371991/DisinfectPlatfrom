package com.example.disinfectplatfrom.Config;

import com.example.disinfectplatfrom.Filter.LoginFilter;
import com.example.disinfectplatfrom.Service.MyUserDetailService;
import com.example.disinfectplatfrom.Service.ServiceImpl.MyPersistentTokenBasedRemeberMeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final MyUserDetailService myUserDetailService;

    @Autowired
    public SecurityConfig(MyUserDetailService myUserDetailService,DataSource dataSource) {
        this.dataSource=dataSource;
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
                .mvcMatchers("/test").permitAll()
                .mvcMatchers("/test1").permitAll()
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
                .rememberMe() //开启记住我功能
                .rememberMeServices(rememberMeServices())//设置自动登录使用哪个 rememberMeServices
//                .tokenRepository(persistentTokenRepository())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req,resp,ex)->{
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请认证之后再去处理！");
                })
                .and()
                .cors()//跨域处理方案
                .configurationSource(corsConfiguration())//跨域未测试
                .and()
                .csrf().disable()
                .sessionManagement()//开启会话管理
                .maximumSessions(1)//单点登录未测试
                .maxSessionsPreventsLogin(true)
                .expiredSessionStrategy((event -> {
                    HttpServletResponse response = event.getResponse();
                    Map<String, Object> res = new HashMap<>();
                    res.put("status",500);
                    res.put("msg","当前会话已经失效,请重新登录！");
                    String s = new ObjectMapper().writeValueAsString(res);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(s);
                    response.flushBuffer();
                }))
//                .maxSessionsPreventsLogin(true)一旦登录，禁止再次登录
                ;//

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    //springsecurity跨域处理方案
    @Bean
    public CorsConfigurationSource corsConfiguration(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;

    }
    //监听会话
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
    //指定数据库持久化
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setCreateTableOnStartup(false);//只需要没有表时设置为 true
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
    @Bean
    public RememberMeServices rememberMeServices(){
        return new MyPersistentTokenBasedRemeberMeServiceImpl(UUID.randomUUID().toString(),userDetailsService(), persistentTokenRepository());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //自定义filter交给工厂管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证 url
        loginFilter.setUsernameParameter("username");//指定接收json用户名key
        loginFilter.setPasswordParameter("password");//指定接收json密码key
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setRememberMeServices(rememberMeServices());//设置认证成功时使用自定义rememberMeServices

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
