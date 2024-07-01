package dev.wanheng.springbootlibrary.service;

import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /**
     * 用户注册
     * @param registerDto
     */
    void register(RegisterDto registerDto);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    UserInfoDto getUserByUsername(String username);

    /**
     * 用户列表
     * @return
     */
    List<LoginDto> getUserList();
}
