package com.violet.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.violet.user.enums.UserGenderStatus;
import com.violet.user.enums.UserStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户头像
     */
    private String headPortrait;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户介绍
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 用户性别1男、2女
     */
    private UserGenderStatus gender;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户状态1正常、2禁用
     */
    private UserStatus status;

}
