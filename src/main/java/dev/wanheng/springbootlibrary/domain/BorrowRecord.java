package dev.wanheng.springbootlibrary.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("borrow_record")
@Data
public class BorrowRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("book_id")
    private Long bookId;

    @TableField("borrow_date")
    private LocalDateTime borrowDate;

    @TableField("return_date")
    private LocalDateTime returnDate;

    @TableField("status")
    private String status;

}
