package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("/comment")
    public String allCommentsForBook(Model model, @RequestParam(name = "bookId") long bookId) {
        var book = bookService.findById(bookId).orElseThrow(NotFoundException::new);
        var comments = commentService.findByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "comment/list";
    }
}
