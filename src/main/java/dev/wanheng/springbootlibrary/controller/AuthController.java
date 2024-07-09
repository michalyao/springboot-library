package dev.wanheng.springbootlibrary.controller;

import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.LoginRequestDto;
import dev.wanheng.springbootlibrary.dto.LoginResponseDto;
import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public PlainResult<String> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return PlainResult.success("注册成功");
    }

    @PostMapping("/login")
    public PlainResult<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return PlainResult.success(loginResponseDto);
    }
}


