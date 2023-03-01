package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Project {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String projectname;
    private int adminid;
    private String creattime;
    private String updatetime;
    private String remark;
    private String originaccount;
    private int c_account;
    private int max_account;
    private int delflag;
}
