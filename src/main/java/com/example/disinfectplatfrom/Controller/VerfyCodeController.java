package com.example.disinfectplatfrom.Controller;

import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Utils.R;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

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
    public R getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        System.out.println("vc sessionid:"+session.getId());
        //1.生成验证码
        String text=producer.createText();
        System.out.println("验证码"+text);
        //2.放入session/redis实现
        session.setAttribute("kaptcha",text);
        //3.生成图片
        BufferedImage image = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image,"jpg",fos);
        //4.返回base64
        //前端要在返回的String前拼接data:image/png;base64
        R r = new R();
        r.setData("data:image/png;base64,"+Base64.encodeBase64String(fos.toByteArray()));
        return r;
    }


    @GetMapping("/isLogin")
    public R isLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        HashMap<String, Object> map = new HashMap<>();

        if ("anonymousUser".equals(principal)){
            map.put("isLogin",false);
            map.put("user",null);
            return R.ok(map);
        }
        User user = (User)principal;
        user.setPassword(null);
        map.put("user",user);
        if (!ObjectUtils.isEmpty(user)){
            map.put("isLogin",true);
        }else{
            map.put("isLogin",false);
        }
        return R.ok(map);


    }
}
