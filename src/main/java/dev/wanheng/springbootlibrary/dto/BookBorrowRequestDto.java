package dev.wanheng.springbootlibrary.dto;

import lombok.Data;

@Data
public class BookBorrowRequestDto {
    private Long userId;
    private Long bookId;
}
