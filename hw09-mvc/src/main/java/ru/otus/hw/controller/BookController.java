package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.BookService;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String allBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "/book/list";
    }
}
