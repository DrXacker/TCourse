package com.example.TCourse.controller;

import com.example.TCourse.dto.TaskDto;
import com.example.TCourse.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskDtoById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<List<TaskDto>> getUserTasks(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ResponseEntity.ok(taskService.getUserTasks(principal));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<List<TaskDto>> createTask(HttpServletRequest request, @RequestBody TaskDto taskDto) {
        Principal principal = request.getUserPrincipal();
        return ResponseEntity.ok(taskService.createTask(principal, taskDto));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{taskId}")
    public ResponseEntity<List<TaskDto>> updateTask(HttpServletRequest request, @PathVariable("taskId") Long taskId,
                                                    @RequestBody TaskDto taskDto) {
        Principal principal = request.getUserPrincipal();
        return ResponseEntity.ok(taskService.updateTask(principal, taskId, taskDto));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteTask(HttpServletRequest request, @PathVariable("taskId") Long taskId){
        Principal principal = request.getUserPrincipal();
        return ResponseEntity.ok(taskService.deleteTask(principal, taskId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/searchAll")
    public ResponseEntity<List<TaskDto>> searchTasksAllRows(HttpServletRequest request,
                                                            @RequestParam(required = false) String body,
                                                            @RequestParam(required = false) String priority,
                                                            @RequestParam(required = false) String category,
                                                            @RequestParam(required = false) LocalDateTime startDateTime,
                                                            @RequestParam(required = false) LocalDateTime endDateTime) {
        Principal principal = request.getUserPrincipal();
        return ResponseEntity.ok(taskService.searchTasksAllRows(principal, body, priority, category,
                startDateTime, endDateTime));
    }
}