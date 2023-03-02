package com.example.disinfectplatfrom.Controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : VerfyCodeController
 * @description : 验证码controller
 * @createTime : 2023/3/2 18:15
 * @updateUser : Lin
 * @updateTime : 2023/3/2 18:15
 * @updateRemark : 描述说明本次修改内容
 */

@RestController
public class VerfyCodeController {
    private final Producer producer;

    @Autowired
    public VerfyCodeController(Producer producer) {
        this.producer=producer;
    }

    @GetMapping("/vc.jpg")
    public String getVerifyCode(HttpSession session) throws IOException {
        //1.生成验证码
        String text=producer.createText();
        //2.放入session/redis实现
        session.setAttribute("kaptcha",text);
        //3.生成图片
        BufferedImage image = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image,"jpg",fos);
        //4.返回base64
        //前端要在返回的String前拼接data:image/png;base64
        return Base64.encodeBase64String(fos.toByteArray());
    }
}
