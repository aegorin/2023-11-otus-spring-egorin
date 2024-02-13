package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreateDto {

    private String text;

    private long bookId;
}
