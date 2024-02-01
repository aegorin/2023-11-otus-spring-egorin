package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Command(group = "Application commands")
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @Command(command = "ag", description = "Show all genres")
    public String showAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
