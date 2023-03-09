package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@Data
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String role_name;
    private String remark;
    private Integer del_flag;
    private Date update_time;


}
