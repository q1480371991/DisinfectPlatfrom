package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@Data
@AllArgsConstructor
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String roleName;
    private String remark;
    private Integer delFlag;
    private String createTime;
    private String updateTime;


}
