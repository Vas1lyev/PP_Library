package ru.vasilyev.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasilyev.springcourse.dao.BookDAO;
import ru.vasilyev.springcourse.dao.PersonDAO;
import ru.vasilyev.springcourse.models.Book;
import ru.vasilyev.springcourse.models.Person;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private BookDAO bookDAO;
    private PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int bookId, Model model, @ModelAttribute Person person) {
        model.addAttribute("book", bookDAO.show(bookId));

        Optional<Person> owner = bookDAO.getOwner(bookId);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book, Model model) {
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{book_id}/edit")
    public String edit(@PathVariable("book_id") int bookId, Model model) {
        model.addAttribute("book", bookDAO.show(bookId));
        return "books/edit";
    }

    @PatchMapping("/{book_id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("book_id") int bookId) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.update(bookId, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int bookId) {
        bookDAO.delete(bookId);
        return "redirect:/books";
    }

    @PatchMapping("/{book_id}/release")
    public String release(@PathVariable("book_id") int bookId) {
        bookDAO.release(bookId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{book_id}/assign")
    public String assign(@PathVariable("book_id") int bookId, @ModelAttribute Person selectedPerson) {
        // у selectedPerson назначено только поле id
        bookDAO.assign(bookId, selectedPerson);
        return "redirect:/books/" + bookId;
    }




}
