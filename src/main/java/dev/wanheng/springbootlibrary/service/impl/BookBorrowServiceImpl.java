package dev.wanheng.springbootlibrary.service.impl;

import dev.wanheng.springbootlibrary.common.LibraryConstants;
import dev.wanheng.springbootlibrary.common.LibraryException;
import dev.wanheng.springbootlibrary.entity.BorrowRecord;
import dev.wanheng.springbootlibrary.entity.User;
import dev.wanheng.springbootlibrary.dto.*;
import dev.wanheng.springbootlibrary.mapper.BorrowRecordMapper;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import dev.wanheng.springbootlibrary.service.BookBorrowService;
import dev.wanheng.springbootlibrary.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.wanheng.springbootlibrary.common.LibraryConstants.STATUS_LEND;
import static dev.wanheng.springbootlibrary.util.RoleUtil.isReader;

@Service
public class BookBorrowServiceImpl implements BookBorrowService {
    @Resource
    private BookService bookService;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public boolean borrowBook(BookBorrowRequestDto bookBorrowRequestDto) {
        Long bookId = bookBorrowRequestDto.getBookId();
        Long userId = bookBorrowRequestDto.getUserId();
        BookDto book = bookService.getBook(bookId);
        if (book == null) {
            throw new LibraryException(500, "图书不存在");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new LibraryException(500, "用户不存在");
        }
        if (!isReader(user)) {
            throw new LibraryException(500, "当前用户不是读者用户");
        }
        BorrowRecord originRecord = borrowRecordMapper.findOneByUserIdAndBookIdAndStatus(userId, bookId, STATUS_LEND);
        if (originRecord != null) {
            throw new LibraryException(500, "不允许重复借书");
        }
        if (book.getStock() <= 0) {
            throw new LibraryException(500, "图书库存不足");
        }
        // 插入借阅记录
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(bookId);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setReturnDate(null);
        borrowRecord.setStatus(STATUS_LEND);
        borrowRecord.setIsbn(book.getIsbn());
        borrowRecord.setUsername(user.getUsername());
        borrowRecord.setUpdatedAt(LocalDateTime.now());
        borrowRecord.setCreatedAt(LocalDateTime.now());
        borrowRecordMapper.insert(borrowRecord);

        // 更新图书库存
        return bookService.decreaseStock(bookId);
    }

    @Override
    @Transactional
    public boolean returnBook(BookReturnRequestDto bookReturnRequestDto) {
        Long bookId = bookReturnRequestDto.getBookId();
        Long userId = bookReturnRequestDto.getUserId();
        BookDto book = bookService.getBook(bookId);
        if (book == null) {
            throw new LibraryException(500, "图书不存在");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new LibraryException(500, "用户不存在");
        }
        if (!isReader(user)) {
            throw new LibraryException(500, "当前用户不是读者用户");
        }
        BorrowRecord originRecord = borrowRecordMapper.findOneByUserIdAndBookIdAndStatus(userId, bookId, STATUS_LEND);
        if (originRecord == null) {
            // 无借阅记录直接返回
            return true;
        }
        originRecord.setReturnDate(LocalDateTime.now());
        originRecord.setStatus(LibraryConstants.STATUS_RETURN);
        originRecord.setUpdatedAt(LocalDateTime.now());
        borrowRecordMapper.updateById(originRecord);
        // 更新图书库存
        return bookService.increaseStock(bookId);

    }

    @Override
    public List<BorrowRecord> findBorrowBooks(List<Long> bookIds, Long userId) {
        List<BorrowRecord> borrowBooks = borrowRecordMapper.findAllByBookIdInAndStatusAndUserId(bookIds, STATUS_LEND, userId);
        if (CollectionUtils.isEmpty(borrowBooks)) {
            return Collections.emptyList();
        }
        return borrowBooks;
    }

    @Override
    public UserBorrowBooksInfoDto getUserBorrowBooksInfo(Long userId) {
        List<BorrowRecord> borrowRecords = borrowRecordMapper.findAllByUserIdAndStatus(userId, STATUS_LEND);
        if (CollectionUtils.isEmpty(borrowRecords)) {
            UserBorrowBooksInfoDto userBorrowBooksInfoDto = new UserBorrowBooksInfoDto();
            userBorrowBooksInfoDto.setBorrowNum(0);
            userBorrowBooksInfoDto.setDelayedBooks(Collections.emptyList());
            return userBorrowBooksInfoDto;
        }
        UserBorrowBooksInfoDto userBorrowBooksInfoDto = new UserBorrowBooksInfoDto();
        userBorrowBooksInfoDto.setBorrowNum(borrowRecords.size());
        List<BorrowRecord> delayRecords = borrowRecords.stream().
                filter(borrowRecord -> borrowRecord.getBorrowDate().plusDays(30).isBefore(LocalDateTime.now()))
                .toList();
        if (CollectionUtils.isEmpty(delayRecords)) {
            userBorrowBooksInfoDto.setDelayedBooks(Collections.emptyList());
            return userBorrowBooksInfoDto;
        }
        List<BookDto> delayBooks = bookService.findAllByIdIn(delayRecords.stream().map(BorrowRecord::getBookId).collect(Collectors.toList()));
        Map<Long, BookDto> bookDtoMap = delayBooks.stream().collect(Collectors.toMap(BookDto::getId, book -> book));
        List<UserBorrowBookDto> ret = new ArrayList<>();
        for (BorrowRecord delayRecord : delayRecords) {
            UserBorrowBookDto userBorrowBookDto = toUserBorrowBookDto(delayRecord, bookDtoMap);
            ret.add(userBorrowBookDto);
        }
        userBorrowBooksInfoDto.setDelayedBooks(ret);
        return userBorrowBooksInfoDto;
    }

    @Override
    public boolean renewBook(BookBorrowRequestDto bookBorrowRequestDto) {
        Long bookId = bookBorrowRequestDto.getBookId();
        Long userId = bookBorrowRequestDto.getUserId();
        BookDto book = bookService.getBook(bookId);
        if (book == null) {
            throw new LibraryException(500, "图书不存在");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new LibraryException(500, "用户不存在");
        }
        if (!isReader(user)) {
            throw new LibraryException(500, "当前用户不是读者用户");
        }
        BorrowRecord originRecord = borrowRecordMapper.findOneByUserIdAndBookIdAndStatus(userId, bookId, STATUS_LEND);
        if (originRecord == null) {
            // 无借阅记录直接返回
            return true;
        }
        originRecord.setBorrowDate(LocalDateTime.now());
        originRecord.setRenewTimes(originRecord.getRenewTimes() + 1);
        originRecord.setUpdatedAt(LocalDateTime.now());
        borrowRecordMapper.updateById(originRecord);
        return true;
    }

    private static UserBorrowBookDto toUserBorrowBookDto(BorrowRecord delayRecord, Map<Long, BookDto> bookDtoMap) {
        BookDto bookDto = bookDtoMap.get(delayRecord.getBookId());
        UserBorrowBookDto userBorrowBookDto = new UserBorrowBookDto();
        userBorrowBookDto.setId(bookDto.getId());
        userBorrowBookDto.setTitle(bookDto.getTitle());
        userBorrowBookDto.setAuthor(bookDto.getAuthor());
        userBorrowBookDto.setPublisher(bookDto.getPublisher());
        userBorrowBookDto.setIsbn(bookDto.getIsbn());
        userBorrowBookDto.setPublishTime(bookDto.getPublishTime());
        userBorrowBookDto.setStock(bookDto.getStock());
        userBorrowBookDto.setPrice(bookDto.getPrice());
        userBorrowBookDto.setStatus(bookDto.getStatus());
        userBorrowBookDto.setUserId(delayRecord.getUserId());
        userBorrowBookDto.setBorrowDate(delayRecord.getBorrowDate());
        userBorrowBookDto.setDeadDate(delayRecord.getBorrowDate().plusDays(30));
        userBorrowBookDto.setReturnDate(delayRecord.getReturnDate());
        return userBorrowBookDto;
    }


}
