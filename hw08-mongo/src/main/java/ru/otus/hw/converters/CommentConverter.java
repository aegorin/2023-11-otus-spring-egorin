package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {

    public String commentToString(CommentDto commentDto) {
        return "Id: %s, text: %s".formatted(commentDto.getId(), commentDto.getComment());
    }

    public CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId());
    }
}
