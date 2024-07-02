package dev.wanheng.springbootlibrary.service;

import dev.wanheng.springbootlibrary.domain.BorrowRecord;
import dev.wanheng.springbootlibrary.dto.BookBorrowRequestDto;
import dev.wanheng.springbootlibrary.dto.BookDto;
import dev.wanheng.springbootlibrary.dto.BookReturnRequestDto;
import dev.wanheng.springbootlibrary.dto.UserBorrowBooksInfoDto;

import java.util.List;

public interface BookBorrowService {
    boolean borrowBook(BookBorrowRequestDto bookBorrowRequestDto);

    boolean returnBook(BookReturnRequestDto bookReturnRequestDto);

    List<BorrowRecord> findBorrowBooks(List<Long> bookIds, Long userId);

    UserBorrowBooksInfoDto getUserBorrowBooksInfo(Long userId);

    boolean renewBook(BookBorrowRequestDto bookBorrowRequestDto);
}
