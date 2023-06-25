package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Orgnization {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String adminAccount;
    private String locate;
    private String remark;
    private String characteristic;
    private String contact;
    private String phonenumber;
}
