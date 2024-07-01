package dev.wanheng.springbootlibrary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.wanheng.springbootlibrary.dto.BookDto;

import java.util.Collection;
import java.util.List;

public interface BookService {
    void addBook(BookDto bookDto);
    void updateBook(Long id, BookDto bookDto);
    void deleteBook(Long id);
    void batchDelete(List<Long> ids);
    BookDto getBook(Long id);
    IPage<BookDto> getAllBooks(Integer pageNum, Integer pageSize, String bookName, String isbn);
    boolean decreaseStock(Long bookId);
    boolean increaseStock(Long bookId);
    List<BookDto> findAllByIdIn(Collection<Long> idList);
}
