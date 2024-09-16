package ru.vasilyev.springcourse.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.vasilyev.springcourse.models.Book;
import ru.vasilyev.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }


    public Book show(int bookId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?", new Object[]{bookId},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES (?, ?, ?)",
                book.getTitle(),
                book.getAuthor(),
                book.getYear());
    }

    public void update(int bookId, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE book_id=?",
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getYear(),
                bookId);
    }

    public void updateOwner(int bookId, Integer personId) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE book_id=?",
                personId,
                bookId);
    }

    public void delete(int bookId) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?", bookId);
    }

//    public Optional<Person> getOwner(int personId) {
//        return jdbcTemplate.query("SELECT * FROM Person WHERE Person.person_id=?",
//                new Object[]{personId}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
//    }

    public Optional<Person> getOwner(int bookId) {
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.person_id " +
                        "WHERE Book.book_id=?",
                new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void release(int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE book_id=?", bookId);
    }

    public void assign(int bookId, Person selectedPerson) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE book_id=?", selectedPerson.getPersonId(), bookId);

    }
}
