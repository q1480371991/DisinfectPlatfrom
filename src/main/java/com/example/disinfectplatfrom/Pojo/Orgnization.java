package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Orgnization {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private int projectid;
    private String adminaccount;
    private String locate;
    private String remark;
}
