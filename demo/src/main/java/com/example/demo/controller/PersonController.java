package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/person")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/person")
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            updatedPerson.setId(id);
            return ResponseEntity.ok(personRepository.save(updatedPerson));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/person/{p_id}/message")
    public ResponseEntity<List<Message>> getMessagesForPerson(@PathVariable int p_id) {
        Optional<Person> person = personRepository.findById(p_id);
        return person.map(p -> ResponseEntity.ok(p.getMessages())).orElse(ResponseEntity.notFound().build());
    }

}