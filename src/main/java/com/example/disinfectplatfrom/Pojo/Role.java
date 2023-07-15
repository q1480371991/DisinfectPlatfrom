package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String roleName;
    @TableField(value ="remark" )
    private String remark;
    private Integer delFlag;
    private String createTime;
    private String updateTime;


}
