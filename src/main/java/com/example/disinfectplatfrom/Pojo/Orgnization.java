package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Orgnization {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private int projectId;
    private String adminAccount;
    private String locate;
    private String remark;
    private String characteristic;
    private String conact;
    private String phonenumber;
}
