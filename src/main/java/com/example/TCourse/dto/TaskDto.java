package com.example.TCourse.dto;

import com.example.TCourse.entity.constant.Category;
import com.example.TCourse.entity.constant.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String body;
    private Boolean isCompleted;
    private Priority priority;
    private Category category;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}