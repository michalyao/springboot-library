package dev.wanheng.springbootlibrary.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.wanheng.springbootlibrary.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findOneByUsername(@Param("username") String username);
}
