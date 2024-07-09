package dev.wanheng.springbootlibrary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.wanheng.springbootlibrary.common.LibraryException;
import dev.wanheng.springbootlibrary.dto.*;
import dev.wanheng.springbootlibrary.entity.User;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import dev.wanheng.springbootlibrary.service.UserService;
import dev.wanheng.springbootlibrary.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void register(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setAddress(registerDto.getAddress());
        user.setNickName(registerDto.getNickName());
        userMapper.insert(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtUtil.generateToken(loginRequestDto.getUsername()));
        return loginResponseDto;
    }

    @Override
    public UserInfoDto getUserByUsername(String username) {
        User user = userMapper.findOneByUsername(username);
        if (user == null) {
            return null;
        }
        return toUserInfoDto(user);
    }

    private UserInfoDto toUserInfoDto(User user) {
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
    public List<LoginRequestDto> getUserList() {
        return userMapper.selectList(null).stream().map(user -> {
            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUsername(user.getUsername());
            return loginRequestDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateUser(Long id, UserInfoDto userInfoDto) {
        User user = toUser(id, userInfoDto);
        userMapper.updateById(user);
    }

    private static User toUser(Long id, UserInfoDto userInfoDto) {
        User user = new User();
        user.setId(id);
        user.setUsername(userInfoDto.getUsername());
        user.setNickName(userInfoDto.getNickName());
        user.setEmail(userInfoDto.getEmail());
        user.setPhone(userInfoDto.getPhone());
        user.setAddress(userInfoDto.getAddress());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
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

    @Override
    public IPage<UserInfoDto> searchUser(Integer pageNum, Integer pageSize, String name, String phone, String email) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
        wrappers.orderByDesc(User::getUpdatedAt);
        if (StringUtils.hasText(name)) {
            wrappers.like(User::getNickName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrappers.like(User::getPhone, phone);
        }
        if (StringUtils.hasText(email)) {
            wrappers.like(User::getEmail, email);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);
        if (userPage == null) {
            return null;
        }
        return userPage.convert(this::toUserInfoDto);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);

    }

    @Override
    public void batchDelete(List<Long> ids) {
        userMapper.deleteBatchIds(ids);
    }

}
