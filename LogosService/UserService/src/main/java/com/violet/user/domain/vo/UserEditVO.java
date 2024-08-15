package com.violet.user.domain.vo;

import com.violet.api.domain.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEditVO extends UserVO {

    private String phone;

}
