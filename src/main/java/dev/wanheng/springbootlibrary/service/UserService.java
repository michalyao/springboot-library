package dev.wanheng.springbootlibrary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    /**
     * 用户注册
     * @param registerDto
     */
    void register(RegisterDto registerDto);


    /**
     * 用户登录
     * @param loginRequestDto
     * @return
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    UserInfoDto getUserByUsername(String username);

    /**
     * 用户搜索
     * @param pageNum
     * @param pageSize
     * @param name
     * @param phone
     * @param email
     * @return
     */
    IPage<UserInfoDto> searchUser(Integer pageNum, Integer pageSize, String name, String phone, String email);

    /**
     * 更新用户信息
     * @param id
     * @param userInfoDto
     */
    void updateUser(Long id, UserInfoDto userInfoDto);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 更新用户密码
     * @param userPasswordChangeDto
     */
    void updateUserPassword(UserPasswordChangeDto userPasswordChangeDto);
}
