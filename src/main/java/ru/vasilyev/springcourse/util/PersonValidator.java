package ru.vasilyev.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vasilyev.springcourse.models.Person;
import ru.vasilyev.springcourse.services.PeopleService;

@Component
public class PersonValidator implements Validator {


    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (peopleService.findByName(person.getName()) != null) {
            errors.rejectValue("name", "", "Человек с таким именем уже существует");
        }

    }
}
