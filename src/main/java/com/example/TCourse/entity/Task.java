package com.example.TCourse.entity;

import com.example.TCourse.entity.constant.Category;
import com.example.TCourse.entity.constant.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean isCompleted;

    private Priority priority;

    private Category category;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}