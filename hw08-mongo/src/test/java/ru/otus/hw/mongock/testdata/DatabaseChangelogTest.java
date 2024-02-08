package ru.otus.hw.mongock.testdata;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

@ChangeLog(order = "001")
public class DatabaseChangelogTest {

    @ChangeSet(order = "0", id = "dropDB", author = "egorin.av", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "1", id = "books_test", author = "egorin.av", runAlways = true)
    public void insertBooks(MongockTemplate mongoTemplate) {
        for (String idEntity : new String[] {"1", "2", "3"}) {
            var author = mongoTemplate.insert(new Author(idEntity, "Author_" + idEntity));
            var genre = mongoTemplate.insert(new Genre(idEntity, "Genre_" + idEntity));
            mongoTemplate.insert(new Book(idEntity, "Book_" + idEntity, author, genre));
        }
    }

    @ChangeSet(order = "2", id = "comments_test", author = "egorin.av", runAlways = true)
    public void insertComments(MongockTemplate mongoTemplate) {
        var book = mongoTemplate.findById("1", Book.class);
        var comment = new Comment(book);
        comment.setId("1");
        comment.setText("Comment_1_book_1");
        mongoTemplate.insert(comment);

        comment = new Comment(book);
        comment.setId("2");
        comment.setText("Comment_2_book_1");
        mongoTemplate.insert(comment);

        book = mongoTemplate.findById("2", Book.class);
        comment = new Comment(book);
        comment.setId("3");
        comment.setText("Comment_1_book_2");
        mongoTemplate.insert(comment);
    }
}
