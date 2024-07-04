package dev.wanheng.springbootlibrary.entity;


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

    @TableField("isbn")
    private String isbn;

    @TableField("username")
    private String username;

    @TableField("renew_times")
    private Integer renewTimes;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
