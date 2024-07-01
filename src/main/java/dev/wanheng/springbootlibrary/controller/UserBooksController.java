package dev.wanheng.springbootlibrary.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.common.LibraryConstants;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.domain.BorrowRecord;
import dev.wanheng.springbootlibrary.dto.BookBorrowRequestDto;
import dev.wanheng.springbootlibrary.dto.BookDto;
import dev.wanheng.springbootlibrary.dto.BookReturnRequestDto;
import dev.wanheng.springbootlibrary.dto.UserBorrowBooksInfoDto;
import dev.wanheng.springbootlibrary.service.BookBorrowService;
import dev.wanheng.springbootlibrary.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/books")
public class UserBooksController {

    @Resource
    private BookBorrowService bookBorrowService;

    @Resource
    private BookService bookService;

    @PostMapping("/borrow")
    public PlainResult<Boolean> borrowBook(@RequestBody BookBorrowRequestDto bookBorrowRequestDto) {
        boolean ret = bookBorrowService.borrowBook(bookBorrowRequestDto);
        return PlainResult.success(ret);
    }

    @PostMapping("/return")
    public PlainResult<Boolean> returnBook(@RequestBody BookReturnRequestDto returnRequestDto) {
        boolean ret = bookBorrowService.returnBook(returnRequestDto);
        return PlainResult.success(ret);
    }


     @GetMapping ("/info")
    public PlainResult<UserBorrowBooksInfoDto> borrowBooksInfo(@RequestParam(required = true) Long userId) {
         UserBorrowBooksInfoDto userBorrowBooksInfo = bookBorrowService.getUserBorrowBooksInfo(userId);
         return PlainResult.success(userBorrowBooksInfo);
     }


    @GetMapping
    public PlainResult<IPage<BookDto>> getAllBooks(@RequestParam(required = false) Integer pageNum,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String isbn,
                                                   @RequestParam(required = true) Long userId) {
        IPage<BookDto> books = bookService.getAllBooks(pageNum, pageSize, title, isbn);
        if (!CollectionUtils.isEmpty(books.getRecords())) {
            List<Long> bookIds = books.getRecords().stream().map(BookDto::getId).collect(Collectors.toList());
            List<BorrowRecord> borrowBooks = bookBorrowService.findBorrowBooks(bookIds, userId);
            if (!CollectionUtils.isEmpty(borrowBooks)) {
                List<Long> borrowBookIds = borrowBooks.stream().map(BorrowRecord::getBookId).collect(Collectors.toList());
                books.getRecords().forEach(book -> {
                    if (borrowBookIds.contains(book.getId())) {
                        book.setStatus(LibraryConstants.STATUS_LEND);
                    }
                });
            }
        }
        return PlainResult.success(books);
    }


}
