package dev.wanheng.springbootlibrary.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserBorrowBooksInfoDto {
    private int borrowNum;
    private List<UserBorrowBookDto> delayedBooks;
}
