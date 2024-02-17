package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreateDto {

    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;

    @NotNull(message = "Должен быть указан идентификатор книги")
    private Long bookId;
}
