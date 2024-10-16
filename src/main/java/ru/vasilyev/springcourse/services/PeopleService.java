package ru.vasilyev.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vasilyev.springcourse.models.Book;
import ru.vasilyev.springcourse.models.Person;
import ru.vasilyev.springcourse.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Person findByName(String name) {
        return peopleRepository.findByName(name);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Person person = peopleRepository.findById(id).orElse(null);

        if (person != null) {
            Hibernate.initialize(person.getBooks());

            for (Book book : person.getBooks()) {
                long difference = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if (difference > 864000000) {
                    book.setExpired(true);
                }
            }
            return person.getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
