package com.example.demo.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String text;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}