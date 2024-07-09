package dev.wanheng.springbootlibrary.controller;

import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.LoginResponseDto;
import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.service.UserService;
import dev.wanheng.springbootlibrary.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public PlainResult<String> register(@RequestBody RegisterDto registerDto) {
        UserInfoDto user = userService.getUserByUsername(registerDto.getUsername());
        if (user != null) {
            return PlainResult.fail(400, "用户名已存在");
        }
        userService.register(registerDto);
        return PlainResult.success("注册成功");
    }

    @PostMapping("/login")
    public PlainResult<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtUtil.generateToken(loginDto.getUsername()));
        return PlainResult.success(loginResponseDto);
    }
}


