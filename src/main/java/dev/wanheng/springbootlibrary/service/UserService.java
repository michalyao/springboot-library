package dev.wanheng.springbootlibrary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.RegisterDto;
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
     * 删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     * @param ids
     */
    void batchDelete(List<Long> ids);
}
