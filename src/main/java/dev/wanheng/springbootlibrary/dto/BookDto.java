package dev.wanheng.springbootlibrary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    @JsonFormat(locale="zh",timezone="GMT+8", pattern="yyyy-MM-dd")
    private LocalDate publishTime;
    private int stock;
    private BigDecimal price;
    private String status;
}
