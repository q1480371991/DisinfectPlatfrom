package com.example.disinfectplatfrom.Config;

import com.example.disinfectplatfrom.Filter.CrossDomainFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
//解决springboot-vue前后端分离项目跨域session不一致的问题
//@Configuration
public class CrossDomainFilterConfiguration {
    @Autowired
    CrossDomainFilter crossDomainFilter;

    @Bean
    public RegistrationBean myFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(crossDomainFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
}
