package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCommentDto;
import ru.otus.hw.models.BookComment;

@Component
public class BookCommentConverter {

    public String bookCommentToString(BookCommentDto bookCommentDto) {
        return "Id: %d, text: %s".formatted(bookCommentDto.getId(), bookCommentDto.getComment());
    }

    public BookCommentDto from(BookComment bookComment) {
        return new BookCommentDto(
                bookComment.getId(),
                bookComment.getComment(),
                bookComment.getBook().getId());
    }
}
