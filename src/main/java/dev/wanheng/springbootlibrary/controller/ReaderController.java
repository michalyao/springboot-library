package dev.wanheng.springbootlibrary.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reader")
public class ReaderController {

//    @Autowired
//    private ReaderService readerService;
//
//    @GetMapping("/books")
//    public ResponseEntity<?> searchBooks(@RequestParam String keyword) {
//        return ResponseEntity.ok(readerService.searchBooks(keyword));
//    }
//
//    @PostMapping("/borrow/{bookId}")
//    public ResponseEntity<?> borrowBook(@PathVariable Long bookId) {
//        readerService.borrowBook(bookId);
//        return ResponseEntity.ok("Book borrowed successfully");
//    }
//
//    @PostMapping("/return/{bookId}")
//    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
//        readerService.returnBook(bookId);
//        return ResponseEntity.ok("Book returned successfully");
//    }
//
//    @GetMapping("/records")
//    public ResponseEntity<List<BorrowRecord>> getBorrowRecords() {
//        return ResponseEntity.ok(readerService.getBorrowRecords());
//    }
}
