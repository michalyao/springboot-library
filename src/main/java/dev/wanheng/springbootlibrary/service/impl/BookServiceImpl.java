package dev.wanheng.springbootlibrary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.wanheng.springbootlibrary.domain.Book;
import dev.wanheng.springbootlibrary.domain.BorrowRecord;
import dev.wanheng.springbootlibrary.dto.BookDto;
import dev.wanheng.springbootlibrary.mapper.BookMapper;
import dev.wanheng.springbootlibrary.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    @Override
    public void addBook(BookDto bookDto) {
        Book book = convertToBook(bookDto);
        bookMapper.insert(book);
    }

    @Override
    public void updateBook(Long id, BookDto bookDto) {
        Book book = convertToBook(bookDto);
        book.setId(id);
        bookMapper.updateById(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookMapper.deleteById(id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        bookMapper.deleteBatchIds(ids);
    }

    @Override
    public BookDto getBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            return null;
        }
        return toBookDto(book);

    }

    @Override
    public IPage<BookDto> getAllBooks(Integer pageNum, Integer pageSize, String bookName, String isbn) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        LambdaQueryWrapper<Book> wrappers = Wrappers.lambdaQuery();
        if (StringUtils.hasText(bookName)) {
            wrappers.like(Book::getTitle,bookName);
        }
        if (StringUtils.hasText(isbn)) {
            wrappers.like(Book::getIsbn,isbn);
        }
        wrappers.orderByDesc(Book::getUpdatedAt);
        Page<Book> bookPage = bookMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);
        if (bookPage == null) {
            return null;
        }
        return bookPage.convert(this::toBookDto);
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book != null && book.getStock() > 0) {
            int rows = bookMapper.decreaseStock(bookId, book.getVersion());
            return rows > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean increaseStock(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book != null) {
            int rows = bookMapper.increaseStock(bookId, book.getVersion());
            return rows > 0;
        }
        return false;
    }

    @Override
    public List<BookDto> findAllByIdIn(Collection<Long> idList) {
        List<Book> books = bookMapper.findAllByIdIn(idList);
        if (CollectionUtils.isEmpty(books)) {
            return Collections.emptyList();
        }
        return books.stream().map(this::toBookDto).collect(Collectors.toList());
    }

    private BookDto toBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setPrice(book.getPrice());
        bookDto.setStock(book.getStock());
        bookDto.setPublishTime(book.getPublishTime());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setStock(book.getStock());
        return bookDto;
    }

    private static Book convertToBook(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setIsbn(bookDto.getIsbn());
        book.setStock(bookDto.getStock());
        book.setPublishTime(bookDto.getPublishTime());
        book.setPrice(bookDto.getPrice());
        return book;
    }
}
