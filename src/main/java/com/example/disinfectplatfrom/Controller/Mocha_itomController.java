package com.example.disinfectplatfrom.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Pojo.Device;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.OrgnizationService;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Mocha_itomController
 * @description : 运维管理controller
 * @createTime : 2023/6/25 20:31
 * @updateUser : Lin
 * @updateTime : 2023/6/25 20:31
 * @updateRemark : 描述说明本次修改内容
 */
@RestController
@PreAuthorize("hasAuthority('mocha_itom')")
@RequestMapping("/mocha_itom")
public class Mocha_itomController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrgnizationService orgnizationService;
    @Autowired
    private DeviceService deviceService;


    //组织管理页面

    /*
     * @title :ListAllOrgnization
     * @Author :Lin
     * @Description :  返回所有组织信息  仅限项目管理员
     * @Date :14:58 2023/7/15
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListAllOrgnization",method = RequestMethod.GET)
    public R ListAllOrgnization(){
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        Collection<Orgnization> orgnizations = orgnizationService.ListAllOrgnization();
        for (Orgnization orgnization : orgnizations) {
            //查询组织下设备的数量
            Collection<Device> devices = deviceService.ListDeviceByOrignizationId(orgnization.getId());
            HashMap<String, Object> data = new HashMap<>();
            if (ObjectUtils.isEmpty(devices)){
                devices=new ArrayList<>();
            }
            data.put("devicenum",devices.size());
            data.put("orgnization",orgnization);
            list.add(data);
        }
        return R.ok(list);
    }

    /*
     * @title :AddOrganization
     * @Author :Lin
     * @Description :  添加组织并创建组织管理员账号  仅限项目管理员  未完成创建组织管理员账号
     * @Date :14:58 2023/7/15
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddOrganization",method = RequestMethod.POST)
    public R AddOrganization(@RequestBody Orgnization orgnization){
        if (!ObjectUtils.isEmpty(orgnization)){
            userService.AddOrganization(orgnization);
            //创建组织管理员账号
            return R.ok(null);
        }
        return R.fail(null);
    }

    /*
     * @title :SelectOrgnization
     * @Author :Lin
     * @Description :  模糊查询组织  未完成
     * @Date :15:20 2023/7/15
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/SelectOrgnization",method = RequestMethod.GET)
    public R SelectOrgnization(@RequestParam("name") String name,
                               @RequestParam("phonenum") String phonenum){
        Collection<Orgnization> orgnizations = orgnizationService.SelectOrgnization(name, phonenum);

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (Orgnization orgnization : orgnizations) {
            //查询组织下设备的数量
            Collection<Device> devices = deviceService.ListDeviceByOrignizationId(orgnization.getId());
            HashMap<String, Object> data = new HashMap<>();
            if (ObjectUtils.isEmpty(devices)){
                devices=new ArrayList<>();
            }
            data.put("devicenum",devices.size());
            data.put("orgnization",orgnization);
            list.add(data);
        }
        return R.ok(list);
    }

    /*
     * @title :ListUsersByOrgnizationId
     * @Author :Lin
     * @Description : 获取组织下的所有用户信息  仅限项目管理员
     * @Date :18:35 2023/8/28
     * @Param :[orgnizationid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListUsersByOrgnizationId",method = RequestMethod.GET)
    public R ListUsersByOrgnizationId(@RequestParam("orgnizationid") Integer orgnizationid){
        if (!ObjectUtils.isEmpty(orgnizationid)){
            Collection<User> users = userService.ListUserByOrgnizationId(orgnizationid);
            return R.ok(users);
        }

        return R.fail(null);
    }




    //设备功能未理清
}
