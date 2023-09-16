package com.example.disinfectplatfrom.Config;

import com.example.disinfectplatfrom.Filter.LoginFilter;
import com.example.disinfectplatfrom.Handler.*;
import com.example.disinfectplatfrom.Pojo.User;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resources;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.*;

@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final MyUserDetailService myUserDetailService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

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
                .mvcMatchers("/doLogin").permitAll()
                .anyRequest().authenticated()//所有请求必须认证
                .and().formLogin()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .and()
                .rememberMe() //开启记住我功能
                .rememberMeServices(rememberMeServices())//设置自动登录使用哪个 rememberMeServices
//                .tokenRepository(persistentTokenRepository())
                .and()
                .cors()//跨域处理方案
                .configurationSource(corsConfiguration())
//                处理未认证、权限不足
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()

                .sessionManagement()//开启会话管理

                .maximumSessions(1)//单点登录未测试
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy())
                .expiredSessionStrategy((event -> {
                    System.out.println("单点登录");
                    HttpServletResponse response = event.getResponse();
                    Map<String, Object> res = new HashMap<>();
                    res.put("status",500);
                    res.put("msg","当前会话已经失效,请重新登录！");
                    String s = new ObjectMapper().writeValueAsString(res);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(s);
                    response.flushBuffer();
                }))

                ;

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    //springsecurity跨域处理方案
    @Bean
    CorsConfigurationSource corsConfiguration(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;

    }
//    @Bean
//    public CorsConfigurationSource corsConfiguration() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 允许的源
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的HTTP方法
//        configuration.setAllowedHeaders(Arrays.asList("*")); // 允许的请求头
//        configuration.setAllowCredentials(true); // 允许携带凭据
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }


//    //监听会话
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

        loginFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);//认证成功处理
        loginFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);//认证失败处理
        return loginFilter;
    }


}
