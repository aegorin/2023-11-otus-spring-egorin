package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@Command(group = "Application commands")
@RequiredArgsConstructor
public class AuthorCommands {
    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @Command(command = "aa", description = "Show all authors")
    public String showAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
