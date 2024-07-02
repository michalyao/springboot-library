package dev.wanheng.springbootlibrary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.dto.BorrowRecordDto;

public interface BorrowRecordService {

    IPage<BorrowRecordDto> searchRecords(Integer pageNum, Integer pageSize, String username, String isbn, Long userId);

}
