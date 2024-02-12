package ru.otus.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String allBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/book/{id}")
    public String editBook(@PathVariable("id") long bookId, Model model) {
        var book = bookService.findById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/form";
    }

    @DeleteMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }

    @GetMapping("/book")
    public String newBook(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/form";
    }

    @PostMapping(value = "/book", params = {"form_create_new_book"})
    public String saveNewBook(HttpServletRequest request) {
        String bookTitle = request.getParameter("title");
        long authorId = Long.parseLong(request.getParameter("author.id"));
        long genreId = Long.parseLong(request.getParameter("genre.id"));
        bookService.create(bookTitle, authorId, genreId);
        return "redirect:/";
    }

    @PutMapping(value = "/book")
    public String updateBook(HttpServletRequest request) {
        long bookId = Long.parseLong(request.getParameter("id"));
        String bookTitle = request.getParameter("title");
        long authorId = Long.parseLong(request.getParameter("author.id"));
        long genreId = Long.parseLong(request.getParameter("genre.id"));
        bookService.update(bookId, bookTitle, authorId, genreId);
        return "redirect:/";
    }
}
