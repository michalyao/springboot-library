package dev.wanheng.springbootlibrary.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.dto.UserPasswordChangeDto;
import dev.wanheng.springbootlibrary.service.UserService;
import dev.wanheng.springbootlibrary.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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

    @GetMapping("/search")
    public PlainResult<IPage<UserInfoDto>> searchUser(@RequestParam(required = false) Integer pageNum,
                                                      @RequestParam(required = false) Integer pageSize,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String phone,
                                                      @RequestParam(required = false) String email) {
        IPage<UserInfoDto> userInfoDtoIPage = userService.searchUser(pageNum, pageSize, name, phone, email);
        return PlainResult.success(userInfoDtoIPage);

    }
    @DeleteMapping("/{id}")
    public PlainResult<Void> deleteBook(@PathVariable Long id) {
        userService.deleteUser(id);
        return PlainResult.success(null);
    }

    @PostMapping("/deleteBatch")
    public PlainResult<Void> deleteBatch(@RequestBody List<Long> ids) {
        userService.batchDelete(ids);
        return PlainResult.success(null);
    }
}
