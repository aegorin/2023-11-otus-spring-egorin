package ru.otus.hw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldReturnAllCommentsForBook() throws Exception {
        given(commentService.findByBookId(1)).willReturn(List.of(
                new CommentUpdateDto(12L, "comment 1"),
                new CommentUpdateDto(34L, "comment 2")));

        mvc.perform(get("/api/v1/book/1/comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(12)))
                .andExpect(jsonPath("$[0].text", Matchers.is("comment 1")));
    }

    @Test
    void delete_comment_by_id() throws Exception {
        mvc.perform(delete("/api/v1/comment/1"))
                .andExpect(status().isNoContent());
        verify(commentService, only()).deleteCommentById(1);
    }

    @Test
    void should_create_new_comment() throws Exception {
        var addedComment = new CommentCreateDto("Comment 1", 321L);
        given(commentService.addComment(addedComment)).willReturn(new CommentUpdateDto(1L, "Comment 1"));

        mvc.perform(post("/api/v1/comment")
                        .content(toJsonString(addedComment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Comment 1"));

        verify(commentService, only()).addComment(addedComment);
    }

    @Test
    void should_not_create_comment_when_text_is_empty() throws Exception {
        var commentWithoutText = new CommentCreateDto("", 1L);

        mvc.perform(post("/api/v1/comment")
                        .content(toJsonString(commentWithoutText))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("text"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(commentService, never()).addComment(commentWithoutText);
    }

    @Test
    void should_not_create_comment_when_bookId_is_not_set() throws Exception {
        var commentWithoutBook = new CommentCreateDto("comment text test", null);

        mvc.perform(post("/api/v1/comment")
                        .content(toJsonString(commentWithoutBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("bookId"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(commentService, never()).addComment(commentWithoutBook);
    }

    @Test
    void should_update_comment() throws Exception {
        var commentUpdate = new CommentUpdateDto(3L, "new comment text");

        mvc.perform(put("/api/v1/comment/3")
                        .content(toJsonString(commentUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(Matchers.is(Matchers.emptyString())));

        verify(commentService, only()).updateComment(commentUpdate);
    }

    @Test
    void should_not_update_comment_when_text_is_null() throws Exception {
        var commentUpdate = new CommentUpdateDto(13L, null);

        mvc.perform(put("/api/v1/comment/13")
                        .content(toJsonString(commentUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("text"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(commentService, never()).updateComment(commentUpdate);
    }

    @Test
    void should_not_update_comment_when_text_is_empty() throws Exception {
        var commentUpdate = new CommentUpdateDto(133L, "  ");

        mvc.perform(put("/api/v1/comment/133")
                        .content(toJsonString(commentUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("text"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(commentService, never()).updateComment(commentUpdate);
    }

    @Test
    void should_not_update_comment_when_commentId_is_null() throws Exception {
        var commentUpdate = new CommentUpdateDto(null, "comment without id");

        mvc.perform(put("/api/v1/comment/7")
                        .content(toJsonString(commentUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", not(Matchers.empty())))
                .andExpect(jsonPath("$.errors[0].field").value("id"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verify(commentService, never()).updateComment(commentUpdate);
    }

    private String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
