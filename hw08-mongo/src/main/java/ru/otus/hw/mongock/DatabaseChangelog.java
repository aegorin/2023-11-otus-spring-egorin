package ru.otus.hw.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "1", id = "init_authors", author = "egorin.av")
    public void addAuthors(MongockTemplate mongoTemplate) {
        var author = new Author("1", "Lewis Carroll");
        mongoTemplate.insert(author);

        author = new Author("2", "Alexandre Dumas");
        mongoTemplate.insert(author);

        author = new Author("3", "Leo Tolstoy");
        mongoTemplate.insert(author);
    }

    @ChangeSet(order = "2", id = "init_genres", author = "egorin.av")
    public void addGenres(MongockTemplate mongoTemplate) {
        var genre = new Genre("1", "Fantasy");
        mongoTemplate.insert(genre);

        genre = new Genre("2", "Adventure");
        mongoTemplate.insert(genre);

        genre = new Genre("3", "Romance");
        mongoTemplate.insert(genre);
    }

    @ChangeSet(order = "3", id = "init_books", author = "egorin.av")
    public void addBooks(MongockTemplate mongoTemplate) {
        var bookTitles = new String[] {
                "Alice’s Adventures in Wonderland",
                "The Three Musketeers",
                "Anna Karenina"};
        int number = 1;
        for (String bookTitle : bookTitles) {
            String id = Integer.toString(number++);
            var author = mongoTemplate.findById(id, Author.class);
            var genre = mongoTemplate.findById(id, Genre.class);
            var book = new Book(id, bookTitle, author, genre);
            mongoTemplate.insert(book);
        }
    }

    @ChangeSet(order = "4", id = "init_comments", author = "egorin.av")
    public void addComments(MongockTemplate mongoTemplate) {
        int commentId = 1;
        for (BookComment bookComment : prepareBookComments()) {
            var book = mongoTemplate.findById(bookComment.bookId(), Book.class);
            for (String text : bookComment.commentText()) {
                Comment comment = new Comment(book);
                comment.setId(Integer.toString(commentId++));
                comment.setComment(text);
                mongoTemplate.insert(comment);
            }
        }
    }

    private List<BookComment> prepareBookComments() {
        List<BookComment> bookComments = new ArrayList<>();
        bookComments.add(new BookComment("1", "Is a quick and easy read"));
        bookComments.add(new BookComment("2",
                "One for all and all for one",
                "The first part of the Merleçon Ballet!",
                "The second part of the Merleçon Ballet!"));
        bookComments.add(new BookComment("3",
                "Rightfully a classic. Outstanding character depictions.",
                "Amazing book......"));
        return bookComments;
    }

    private record BookComment(String bookId, String... commentText){}
}
