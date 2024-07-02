package dev.wanheng.springbootlibrary.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.mapper.BookMapper;
import dev.wanheng.springbootlibrary.mapper.BorrowRecordMapper;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashBoardController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @GetMapping("/stats")
    public PlainResult<Map<String, Object>> getStatistics() {
        // 查询用户数
        long userCount = userMapper.selectCount(new QueryWrapper<>());

        // 查询借阅记录数
        long lendRecordCount = borrowRecordMapper.selectCount(new QueryWrapper<>());

        // 查询图书数
        long bookCount = bookMapper.selectCount(new QueryWrapper<>());

        // 构造返回的统计数据
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("userCount", userCount);
        statsMap.put("lendRecordCount", lendRecordCount);
        statsMap.put("bookCount", bookCount);

        return PlainResult.success(statsMap);
    }

}
