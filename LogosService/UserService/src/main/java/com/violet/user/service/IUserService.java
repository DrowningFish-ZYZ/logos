package com.violet.user.service;

import com.violet.common.domain.Result;
import com.violet.user.domain.dto.UserEditFormDTO;
import com.violet.user.domain.dto.UserLoginFormDTO;
import com.violet.user.domain.dto.UserRegisterDTO;
import com.violet.user.domain.vo.UserLoginVO;

import java.util.List;
import java.util.Set;

public interface IUserService {

    UserLoginVO login(UserLoginFormDTO dto);

    Result getUserById(Long id);

    Result getLoggedUser();

    Result getUsersByIds(Set<Long> ids);

    Result updateLoggedUser(UserEditFormDTO dto);

    Result register(UserRegisterDTO dto);

    Result getCode(String phone);

    Result getLoginCode(String phone);
}
