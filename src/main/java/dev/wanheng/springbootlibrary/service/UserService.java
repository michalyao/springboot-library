package dev.wanheng.springbootlibrary.service;

import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.dto.UserPasswordChangeDto;
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

    /**
     * 更新用户信息
     * @param id
     * @param userInfoDto
     */
    void updateUser(Long id, UserInfoDto userInfoDto);

    /**
     * 更新用户密码
     * @param userPasswordChangeDto
     */
    void updateUserPassword(UserPasswordChangeDto userPasswordChangeDto);

}
