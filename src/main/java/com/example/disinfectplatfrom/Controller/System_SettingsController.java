package com.example.disinfectplatfrom.Controller;

import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : System_SettingsController
 * @description : 描述说明该类的功能
 * @createTime : 2023/7/16 17:51
 * @updateUser : Lin
 * @updateTime : 2023/7/16 17:51
 * @updateRemark : 描述说明本次修改内容
 */

@RequestMapping("/system_settings")
@RestController
public class System_SettingsController {

    @Autowired
    private UserService userService;


    /*
     * @title :UpdatePassword
     * @Author :Lin
     * @Description : 修改账号密码
     * @Date :17:57 2023/7/16
     * @Param :[data]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/UpdatePassword",method = RequestMethod.POST)
    public R UpdatePassword(@RequestBody Map<String,Object> data){
        if (!ObjectUtils.isEmpty(data)){
            String  password = (String) data.get("password");
            userService.UpdatePassword(password);
            return R.ok(null);
        }
        return R.fail(null);
    }

    @RequestMapping(value = "/UpdatePhonenumber",method = RequestMethod.POST)
    public R UpdatePhonenumber(@RequestBody Map<String,Object> data){
        if (!ObjectUtils.isEmpty(data)){
            String  phonenumber = (String) data.get("phonenumber");
            userService.UpdatePhonenumber(phonenumber);
            return R.ok(null);
        }
        return R.fail(null);
    }

    @RequestMapping(value = "/UpdateEmail",method = RequestMethod.POST)
    public R UpdateEmail(@RequestBody Map<String,Object> data){
        if (!ObjectUtils.isEmpty(data)){
            String  email = (String) data.get("email");
            userService.UpdateEmail(email);
            return R.ok(null);
        }
        return R.fail(null);
    }
}
