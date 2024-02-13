package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Map;

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
    public ModelAndView saveNewBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var model = Map.of(
                    "book", bookCreateDto,
                    "genres", genreService.findAll(),
                    "authors", authorService.findAll());
            return new ModelAndView("book/form_new_book", model, HttpStatus.BAD_REQUEST);
        }
        bookService.create(bookCreateDto);
        return new ModelAndView("redirect:/");
    }

    @PutMapping(value = "/book")
    public ModelAndView updateBook(@Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var model = Map.of(
                    "book", bookUpdateDto,
                    "genres", genreService.findAll(),
                    "authors", authorService.findAll());
            return new ModelAndView("book/form_edit_book", model, HttpStatus.BAD_REQUEST);
        }
        bookService.update(bookUpdateDto);
        return new ModelAndView("redirect:/");
    }
}
