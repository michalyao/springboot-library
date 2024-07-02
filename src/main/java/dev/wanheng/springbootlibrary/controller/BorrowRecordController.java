package dev.wanheng.springbootlibrary.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.BorrowRecordDto;
import dev.wanheng.springbootlibrary.service.BorrowRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrow")
public class BorrowRecordController {

    @Resource
    private BorrowRecordService borrowRecordService;

    @GetMapping("/record")
    public PlainResult<IPage<BorrowRecordDto>> searchRecord(@RequestParam(required = false) Integer pageNum,
                                                            @RequestParam(required = false) Integer pageSize,
                                                            @RequestParam(required = false) String isbn,
                                                            @RequestParam(required = false) String username) {

        IPage<BorrowRecordDto> borrowRecordDtoIPage = borrowRecordService.searchRecords(pageNum, pageSize, username, isbn, null);
        return PlainResult.success(borrowRecordDtoIPage);
    }

    @GetMapping("/user/record")
    public PlainResult<IPage<BorrowRecordDto>> searchUserRecord(@RequestParam(required = false) Integer pageNum,
                                                                @RequestParam(required = false) Integer pageSize,
                                                                @RequestParam(required = false) String isbn,
                                                                @RequestParam(required = true) Long userId) {

        IPage<BorrowRecordDto> borrowRecordDtoIPage = borrowRecordService.searchRecords(pageNum, pageSize, null, isbn, userId);
        return PlainResult.success(borrowRecordDtoIPage);
    }
}
