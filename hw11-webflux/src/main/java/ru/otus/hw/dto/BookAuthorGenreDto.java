package ru.otus.hw.dto;

public record BookAuthorGenreDto(long id, String title,
                                 long authorId, String authorName,
                                 long genreId, String genreName) {}
