package dev.wanheng.springbootlibrary.controller;

import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.dto.UserPasswordChangeDto;
import dev.wanheng.springbootlibrary.service.UserService;
import dev.wanheng.springbootlibrary.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
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

    @PutMapping("/{id}")
    public PlainResult<Void> updateUser(@PathVariable Long id, @RequestBody UserInfoDto userInfoDto) {
        userService.updateUser(id, userInfoDto);
        return PlainResult.success(null);
    }

    @PostMapping("/password")
    public PlainResult<Void> updateUserPassword(@RequestBody UserPasswordChangeDto userPasswordChangeDto) {
        userService.updateUserPassword(userPasswordChangeDto);
        return PlainResult.success(null);
    }
}
