package com.violet.user.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import com.violet.user.domain.vo.UserEditVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEditFormDTO extends UserEditVO implements DTOTemplate {

    private String oldPwd;
    private String newPwd;

    @Override
    public void verify() {
        if (getUsername() == null || getUsername().trim().isEmpty()) throw new ParameterException("用户名不能为空");
        if (getPhone() == null || getPhone().trim().isEmpty()) throw new ParameterException("手机号不能为空");
        if (getGender() == null || getGender() > 1 || getGender() < 0) throw new ParameterException("异常的性别");
        if (getDesc() == null || getDesc().trim().isEmpty()) throw new ParameterException("描述不能为空");
        if (getHeadPortrait() == null || getHeadPortrait().trim().isEmpty()) throw new ParameterException("头像不能为空");
    }
}
