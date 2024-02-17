package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto {

    @NotNull(message = "Идентификатор книги обязателен к заполнению")
    private Long id;

    @NotBlank(message = "Наименование книги не должно быть пустым")
    @Size(min = 2, max = 256, message = "Наименование книги должно содержать от 2 до 256 символов")
    private String title;

    @NotNull(message = "Автор книги должен быть указан")
    private Long authorId;

    @NotNull(message = "Жанр книги должен быть определён")
    private Long genreId;
}
