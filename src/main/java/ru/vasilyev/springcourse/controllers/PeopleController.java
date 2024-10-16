package ru.vasilyev.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasilyev.springcourse.models.Person;
import ru.vasilyev.springcourse.services.PeopleService;
import ru.vasilyev.springcourse.util.PersonValidator;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonValidator personValidator,
                            PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }


    @GetMapping("/{person_id}")
    public String show(@PathVariable("person_id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));


        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{person_id}/edit")
    public String edit(@PathVariable("person_id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{person_id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("person_id") int id) {
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{person_id}")
    public String delete(@PathVariable("person_id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

}
