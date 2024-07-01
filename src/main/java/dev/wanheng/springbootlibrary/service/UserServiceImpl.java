package dev.wanheng.springbootlibrary.service;

import dev.wanheng.springbootlibrary.domain.User;
import dev.wanheng.springbootlibrary.dto.RegisterDto;
import dev.wanheng.springbootlibrary.dto.LoginDto;
import dev.wanheng.springbootlibrary.dto.UserInfoDto;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
