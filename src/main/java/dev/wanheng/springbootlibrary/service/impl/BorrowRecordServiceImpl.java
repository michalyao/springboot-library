package dev.wanheng.springbootlibrary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.wanheng.springbootlibrary.domain.Book;
import dev.wanheng.springbootlibrary.domain.BorrowRecord;
import dev.wanheng.springbootlibrary.domain.User;
import dev.wanheng.springbootlibrary.dto.BorrowRecordDto;
import dev.wanheng.springbootlibrary.mapper.BookMapper;
import dev.wanheng.springbootlibrary.mapper.BorrowRecordMapper;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import dev.wanheng.springbootlibrary.service.BorrowRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.wanheng.springbootlibrary.common.LibraryConstants.MAX_RENEW_TIMES;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @Resource
    private BookMapper bookMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public IPage<BorrowRecordDto> searchRecords(Integer pageNum, Integer pageSize, String username, String isbn, Long userId) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        LambdaQueryWrapper<BorrowRecord> wrappers = Wrappers.lambdaQuery();
        wrappers.orderByDesc(BorrowRecord::getUpdatedAt);
        if (StringUtils.hasText(username)) {
            wrappers.like(BorrowRecord::getUsername, username);
        }
        if (StringUtils.hasText(isbn)) {
            wrappers.like(BorrowRecord::getIsbn, isbn);
        }
        if (Objects.nonNull(userId)) {
            wrappers.eq(BorrowRecord::getUserId, userId);
        }
        Page<BorrowRecord> borrowRecordPage = borrowRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);
        if (borrowRecordPage == null) {
            return null;
        }
        List<BorrowRecord> records = borrowRecordPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }
        List<Long> bookIds = records.stream().map(BorrowRecord::getBookId).collect(Collectors.toList());
        List<Book> books = bookMapper.selectBatchIds(bookIds);
        Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));

        List<Long> userIds = records.stream().map(BorrowRecord::getUserId).toList();
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap  = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        return borrowRecordPage.convert(borrowRecord -> toBorrowRecordDto(borrowRecord, bookMap, userMap));
    }

    private BorrowRecordDto toBorrowRecordDto(BorrowRecord borrowRecord, Map<Long, Book> bookMap, Map<Long, User> userMap) {
        BorrowRecordDto borrowRecordDto = new BorrowRecordDto();
        borrowRecordDto.setIsbn(borrowRecord.getIsbn());
        borrowRecordDto.setTitle(bookMap.get(borrowRecord.getBookId()).getTitle());
        borrowRecordDto.setUsername(borrowRecord.getUsername());
        borrowRecordDto.setNickName(userMap.get(borrowRecord.getUserId()).getNickName());
        borrowRecordDto.setBorrowDate(borrowRecord.getBorrowDate());
        borrowRecordDto.setReturnDate(borrowRecord.getReturnDate());
        borrowRecordDto.setDeadDate(borrowRecord.getBorrowDate().plusDays(30));
        borrowRecordDto.setStatus(borrowRecord.getStatus());
        borrowRecordDto.setRenewTimes(MAX_RENEW_TIMES - borrowRecord.getRenewTimes());
        borrowRecordDto.setBookId(borrowRecord.getBookId());
        borrowRecordDto.setUserId(borrowRecord.getUserId());
        return borrowRecordDto;
    }
}
