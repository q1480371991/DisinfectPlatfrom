package com.example.disinfectplatfrom.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
