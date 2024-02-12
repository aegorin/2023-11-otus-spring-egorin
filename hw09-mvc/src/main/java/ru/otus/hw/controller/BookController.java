package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
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
        var bookDto = bookService.findById(bookId);
        var bookUpdateDto = new BookUpdateDto(bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());
        model.addAttribute("book", bookUpdateDto);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/form_edit_book";
    }

    @DeleteMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }

    @GetMapping("/book")
    public String newBook(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/form_new_book";
    }

    @PostMapping(value = "/book")
    public String saveNewBook(@ModelAttribute("book") BookCreateDto bookCreateDto) {
        bookService.create(bookCreateDto);
        return "redirect:/";
    }

    @PutMapping(value = "/book")
    public String updateBook(@ModelAttribute("book") BookUpdateDto bookUpdateDto) {
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }
}
