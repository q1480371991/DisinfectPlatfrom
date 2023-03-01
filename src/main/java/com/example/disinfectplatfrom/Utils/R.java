package com.example.disinfectplatfrom.Utils;

import lombok.Data;

@Data
//数据工具类
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


}
