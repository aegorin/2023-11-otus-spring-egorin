package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateDto {

    @NotBlank(message = "Наименование книги не должно быть пустым")
    @Size(min = 2, max = 256, message = "Наименование книги должно содержать от 2 до 256 символов")
    private String title;

    private long authorId;

    private long genreId;
}
