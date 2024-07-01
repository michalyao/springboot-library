package dev.wanheng.springbootlibrary.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private Long id;
    private String username;
    private String role;
    private String nickName;
    private String address;
    private String phone;
    private String email;
}
