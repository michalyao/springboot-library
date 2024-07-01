package dev.wanheng.springbootlibrary.service;

import dev.wanheng.springbootlibrary.common.LibraryException;
import dev.wanheng.springbootlibrary.domain.User;
import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.dto.UserPasswordChangeDto;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setAddress(registerDto.getAddress());
        userMapper.insert(user);
    }

    @Override
    public UserInfoDto getUserByUsername(String username) {
        User user = userMapper.findOneByUsername(username);
        if (user == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setNickName(user.getNickName());
        userInfoDto.setAddress(user.getAddress());
        userInfoDto.setPhone(user.getPhone());
        userInfoDto.setEmail(user.getEmail());
        userInfoDto.setId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setRole(user.getRole());
        return userInfoDto;
    }

    @Override
    public List<LoginDto> getUserList() {
        return userMapper.selectList(null).stream().map(user -> {
            LoginDto loginDto = new LoginDto();
            loginDto.setUsername(user.getUsername());
            return loginDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateUser(Long id, UserInfoDto userInfoDto) {
        User user = new User();
        user.setId(id);
        user.setUsername(userInfoDto.getUsername());
        user.setNickName(userInfoDto.getNickName());
        user.setEmail(userInfoDto.getEmail());
        user.setPhone(userInfoDto.getPhone());
        user.setAddress(userInfoDto.getAddress());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public void updateUserPassword(UserPasswordChangeDto userPasswordChangeDto) {
        if (userPasswordChangeDto.getUserId() == null) {
            throw new LibraryException(400, "用户ID不能为空");
        }
        if (!StringUtils.hasText(userPasswordChangeDto.getOldPassword())) {
            throw new LibraryException(400, "旧密码不能为空");
        }
        if (!StringUtils.hasText(userPasswordChangeDto.getNewPassword())) {
            throw new LibraryException(400, "新密码不能为空");
        }
        if (Objects.equals(userPasswordChangeDto.getOldPassword(), userPasswordChangeDto.getNewPassword())) {
            throw new LibraryException(400, "新密码不能与旧密码相同");
        }
        User user = userMapper.selectById(userPasswordChangeDto.getUserId());
        if (user == null) {
            throw new LibraryException(400, "用户不存在");
        }
        if (!passwordEncoder.matches(userPasswordChangeDto.getOldPassword(), user.getPassword())) {
            throw new LibraryException(500, "旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(userPasswordChangeDto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }


}
