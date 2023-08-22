package com.example.disinfectplatfrom.Utils;

import com.example.disinfectplatfrom.Pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


public class R {
    private  Boolean flag;//响应状态
    private  String msg;//响应信息
    private Object data;//响应数据
    private  Integer code;//响应码
    public R(Boolean flag)
    {
        this.flag=flag;
    }
    public R() {}


    public R(Object data) {
        this.data=data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public String getMsg() {
        return msg;
    }

    public R(Boolean flag, String msg, Object data, Integer code) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    @Override
    public String toString() {
        return "R{" +
                "flag=" + flag +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }

    public Object getData() {
        return data;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
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
