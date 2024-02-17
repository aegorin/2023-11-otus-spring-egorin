package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUpdateDto {
    @NotNull
    private Long id;

    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;
}
