package dev.wanheng.springbootlibrary.controller;

import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.service.UserService;
import dev.wanheng.springbootlibrary.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private UserService userService;

    @GetMapping
    public PlainResult<UserInfoDto> getUserInfo(@RequestParam String token) {
        String username = jwtUtil.extractUsername(token);
        UserInfoDto userInfoDto = userService.getUserByUsername(username);
        return PlainResult.success(userInfoDto);
    }
}
