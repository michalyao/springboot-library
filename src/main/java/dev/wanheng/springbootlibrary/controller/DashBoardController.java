package dev.wanheng.springbootlibrary.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import dev.wanheng.springbootlibrary.common.PlainResult;
import dev.wanheng.springbootlibrary.domain.Book;
import dev.wanheng.springbootlibrary.domain.BorrowRecord;
import dev.wanheng.springbootlibrary.mapper.BookMapper;
import dev.wanheng.springbootlibrary.mapper.BorrowRecordMapper;
import dev.wanheng.springbootlibrary.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        // 获取当前日期和一周前的日期
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusWeeks(1);

        // 查询最近一周的借阅记录
        LambdaQueryWrapper<BorrowRecord> borrowRecordQuery = Wrappers.lambdaQuery();
        borrowRecordQuery.between(BorrowRecord::getBorrowDate, oneWeekAgo, today);
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(borrowRecordQuery);

        // 按日期统计借阅数量
        Map<LocalDate, Long> borrowCountPerDay = borrowRecords.stream()
                .collect(Collectors.groupingBy(borrowRecord -> borrowRecord.getBorrowDate().toLocalDate(), Collectors.counting()));

        // 确保结果包含一周内的所有日期，即使某些日期没有记录
        List<Map<String, Object>> dailyLendRecords = new ArrayList<>();
        for (LocalDate date = oneWeekAgo; !date.isAfter(today); date = date.plusDays(1)) {
            Map<String, Object> record = new HashMap<>();
            record.put("date", date.toString());
            record.put("count", borrowCountPerDay.getOrDefault(date, 0L));
            dailyLendRecords.add(record);
        }
        statsMap.put("dailyLendRecords", dailyLendRecords);

        return PlainResult.success(statsMap);
    }

}
