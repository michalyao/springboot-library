package dev.wanheng.springbootlibrary.mapper;
import java.util.List;
import java.util.Collection;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.wanheng.springbootlibrary.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    int decreaseStock(@Param("id") Long id, @Param("version") Integer version);

    int increaseStock(@Param("id") Long id, @Param("version") Integer version);

}
