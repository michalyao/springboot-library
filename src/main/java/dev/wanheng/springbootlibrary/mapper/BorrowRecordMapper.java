package dev.wanheng.springbootlibrary.mapper;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.wanheng.springbootlibrary.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    BorrowRecord findOneByUserIdAndBookIdAndStatus(@Param("userId") Long userId, @Param("bookId") Long bookId, @Param("status") String status);

    List<BorrowRecord> findAllByBookIdInAndStatusAndUserId(@Param("bookIdList") Collection<Long> bookIdList, @Param("status") String status, @Param("userId") Long userId);

    List<BorrowRecord> findAllByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
