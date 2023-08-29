package com.example.disinfectplatfrom.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Pojo.Device;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.OrgnizationService;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    @PreAuthorize("hasAuthority('organization_management')")
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
    @PreAuthorize("hasAuthority('organization_management')")
    @RequestMapping(value = "/AddOrganization",method = RequestMethod.POST)
    public R AddOrganization(@RequestBody String json) throws JsonProcessingException {
        System.out.println(json);
        if (!ObjectUtils.isEmpty(json)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            User user = mapper.convertValue(jsonNode.get("user"), User.class);
            user.setPassword("{noop}"+user.getPassword());
            Orgnization orgnization = mapper.convertValue(jsonNode.get("organization"), Orgnization.class);
            //创建组织
            orgnization.setAdminAccount(user.getUsername());
            userService.AddOrganization(orgnization);
//            //创建组织管理员账号
            userService.AddOrgnizationAdmin(user,orgnization.getId());
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
    @PreAuthorize("hasAuthority('organization_management')")
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
    @PreAuthorize("hasAuthority('organization_management')")
    @RequestMapping(value = "/ListUsersByOrgnizationId",method = RequestMethod.GET)
    public R ListUsersByOrgnizationId(@RequestParam("orgnizationid") Integer orgnizationid){
        if (!ObjectUtils.isEmpty(orgnizationid)){
            Collection<User> users = userService.ListUserByOrgnizationId(orgnizationid);
            return R.ok(users);
        }

        return R.fail(null);
    }




    //组织管理页面

    /*
     * @title :ListDevice
     * @Author :Lin
     * @Description :  根据登录账号角色的不同获取设备信息   仅限 HW or 项目管理员 or 组织管理员
     * HW：所有设备信息   项目管理员：所管理项目下的所有组织的设备信息  组织管理员：所管理的组织下的设备信息
     * @Date :0:06 2023/8/30
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('device_management')")
    @RequestMapping(value = "/ListDevice",method = RequestMethod.GET)
    public R ListDevice(){
        Collection<Device> devices = deviceService.ListDevice();
        if (!ObjectUtils.isEmpty(devices)){
            return R.ok(devices);
        }
        return R.fail(null);
    }

    /*
     * @title :SelectDevice
     * @Author :Lin
     * @Description : 设备模糊查询
     * @Date :0:09 2023/8/30
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('device_management')")
    @RequestMapping(value = "/SelectDevice",method = RequestMethod.POST)
    public R SelectDevice(){




        return R.fail(null);
    }

    /*
     * @title :AddDevice
     * @Author :Lin
     * @Description :  添加设备 仅限组织管理员
     * @Date :0:13 2023/8/30
     * @Param :[device]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('device_management')")
    @RequestMapping(value = "/AddDevice",method = RequestMethod.POST)
    public R AddDevice(Device device){


        return R.fail(null);
    }

    /*
     * @title :AddDevices
     * @Author :Lin
     * @Description : 批量添加设备  仅限组织管理员
     * @Date :0:13 2023/8/30
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('device_management')")
    @RequestMapping(value = "/AddDevice",method = RequestMethod.POST)
    public R AddDevices(){


        return R.fail(null);
    }
}
