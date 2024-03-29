package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {

    public String commentToString(CommentDto commentDto) {
        return "Id: %d, text: %s".formatted(commentDto.getId(), commentDto.getComment());
    }

    public CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getBook().getId());
    }
}
