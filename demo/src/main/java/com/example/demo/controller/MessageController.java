package com.example.demo.controller;


import com.example.demo.dto.Message;
import com.example.demo.dto.Person;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/message")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        Optional<Message> message = messageRepository.findById(id);
        return message.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/message")
    public Message createMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @PutMapping("/message/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            updatedMessage.setId(id);
            return ResponseEntity.ok(messageRepository.save(updatedMessage));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/person/{p_id}/message")
    public ResponseEntity<Message> createMessageForPerson(@PathVariable int p_id, @RequestBody Message message) {
        Optional<Person> person = personRepository.findById(p_id);
        if (person.isPresent()) {
            message.setPerson(person.get());
            return ResponseEntity.ok(messageRepository.save(message));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/person/{p_id}/message/{m_id}")
    public ResponseEntity<Void> deleteMessageForPerson(@PathVariable int p_id, @PathVariable int m_id) {
        Optional<Person> personOptional = personRepository.findById(p_id);
        if (personOptional.isPresent()) {
            Optional<Message> messageOptional = messageRepository.findById(m_id);
            if (messageOptional.isPresent() && messageOptional.get().getPerson().getId() == p_id) {
                messageRepository.deleteById(m_id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}