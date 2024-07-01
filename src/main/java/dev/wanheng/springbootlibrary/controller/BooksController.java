package dev.wanheng.springbootlibrary.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.dto.BookDto;
import dev.wanheng.springbootlibrary.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BooksController {

    @Resource
    private BookService bookService;

    @PostMapping("/books")
    public PlainResult<Void> addBook(@RequestBody BookDto bookDto) {
        bookService.addBook(bookDto);
        return PlainResult.success(null);
    }

    @PutMapping("/books/{id}")
    public PlainResult<Void> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        bookService.updateBook(id, bookDto);
        return PlainResult.success(null);
    }

    @DeleteMapping("/books/{id}")
    public PlainResult<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return PlainResult.success(null);
    }

    @PostMapping("/books/deleteBatch")
    public PlainResult<Void> deleteBatch(@RequestBody List<Long> ids) {
        bookService.batchDelete(ids);
        return PlainResult.success(null);
    }

    @GetMapping("/books")
    public PlainResult<IPage<BookDto>> getAllBooks(@RequestParam(required = false) Integer pageNum,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String isbn) {
        IPage<BookDto> books = bookService.getAllBooks(pageNum, pageSize, title, isbn);
        return PlainResult.success(books);
    }
}
