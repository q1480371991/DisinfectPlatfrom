package com.example.disinfectplatfrom.Utils;

import com.example.disinfectplatfrom.Pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
//数据工具类
@AllArgsConstructor
public class R {
    private  Boolean flag;//请求状态
    private  String msg;
    private Object data;
    private  Integer code;
    public R(Boolean flag)
    {
        this.flag=flag;
    }
    public R() {}


    public R(Object data) {
        this.data=data;
    }
    public static R ok(Object data){
        R r = new R(data);
        r.setCode(200);
        r.setMsg("请求成功");
        return r;
    }

    public static R fail(Object data){
        R r = new R(data);
        r.setCode(201);
        r.setMsg("请求失败");
        return r;
    }
}
