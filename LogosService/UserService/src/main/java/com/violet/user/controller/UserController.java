package com.violet.user.controller;

import com.violet.common.domain.Result;
import com.violet.user.domain.dto.UserEditFormDTO;
import com.violet.user.domain.dto.UserLoginFormDTO;
import com.violet.user.domain.dto.UserRegisterDTO;
import com.violet.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserLoginFormDTO loginFormDTO) {
        return Result.Builder.buildGetResult(userService.login(loginFormDTO));
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO dto) {
        return userService.register(dto);
    }

    @ApiOperation("发送注册验证码")
    @GetMapping("/code/{phone}")
    public Result getCode(@PathVariable String phone) {
        return userService.getCode(phone);
    }

    @ApiOperation("发送登录验证码")
    @GetMapping("/login/code/{phone}")
    public Result getLoginCode(@PathVariable String phone) {
        return userService.getLoginCode(phone);
    }

    @ApiOperation("根据用户ID获取用户信息")
    @GetMapping
    public Result getUserById(@RequestParam("id") Long id) {
        return userService.getUserById(id);
    }

    @ApiOperation("根据ID列表获取用户列表")
    @GetMapping("/list")
    public Result getUsersByIds(@RequestParam("ids") Set<Long> ids) {
        return userService.getUsersByIds(ids);
    }

    @ApiOperation("获取当前登录用户的信息")
    @GetMapping("/logged")
    public Result getLoggedUser() {
        return userService.getLoggedUser();
    }

    @ApiOperation("修改当前用户信息")
    @PutMapping("/logged")
    public Result updateLoggedUser(@Validated @RequestBody UserEditFormDTO dto) {
        return userService.updateLoggedUser(dto);
    }
}
