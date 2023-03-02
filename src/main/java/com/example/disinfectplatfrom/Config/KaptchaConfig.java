package com.example.disinfectplatfrom.Config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : KaptchaConfig
 * @description : 验证码配置类
 * @createTime : 2023/3/2 18:09
 * @updateUser : Lin
 * @updateTime : 2023/3/2 18:09
 * @updateRemark : 描述说明本次修改内容
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public Producer kaptcha(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","150");
        properties.setProperty("kaptcha.image.heigth","50");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
