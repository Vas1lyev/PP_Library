package ru.vasilyev.springcourse.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vasilyev.springcourse.dao.PersonDAO;
import ru.vasilyev.springcourse.models.Person;

@Component
public class PersonValidator implements Validator {


    private final PersonDAO personDAO;

    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (personDAO.getPersonByName(person.getName()).isPresent()) {
            errors.rejectValue("name", "", "Человек с таким именем уже существует");
        }

    }
}
